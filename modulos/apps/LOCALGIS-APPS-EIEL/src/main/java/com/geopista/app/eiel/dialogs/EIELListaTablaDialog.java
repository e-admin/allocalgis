/**
 * EIELListaTablaDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.app.eiel.dialogs;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.InitEIEL;
import com.geopista.app.eiel.beans.AbastecimientoAutonomoEIEL;
import com.geopista.app.eiel.beans.CabildoConsejoEIEL;
import com.geopista.app.eiel.beans.CaptacionesEIEL;
import com.geopista.app.eiel.beans.CasasConsistorialesEIEL;
import com.geopista.app.eiel.beans.CementeriosEIEL;
import com.geopista.app.eiel.beans.CentrosAsistencialesEIEL;
import com.geopista.app.eiel.beans.CentrosCulturalesEIEL;
import com.geopista.app.eiel.beans.CentrosEnsenianzaEIEL;
import com.geopista.app.eiel.beans.CentrosSanitariosEIEL;
import com.geopista.app.eiel.beans.ColectorEIEL;
import com.geopista.app.eiel.beans.DepositosEIEL;
import com.geopista.app.eiel.beans.Depuradora1EIEL;
import com.geopista.app.eiel.beans.Depuradora2EIEL;
import com.geopista.app.eiel.beans.DiseminadosEIEL;
import com.geopista.app.eiel.beans.EdificiosSinUsoEIEL;
import com.geopista.app.eiel.beans.EmisariosEIEL;
import com.geopista.app.eiel.beans.Encuestados1EIEL;
import com.geopista.app.eiel.beans.Encuestados2EIEL;
import com.geopista.app.eiel.beans.EntidadesAgrupadasEIEL;
import com.geopista.app.eiel.beans.EntidadesSingularesEIEL;
import com.geopista.app.eiel.beans.IncendiosProteccionEIEL;
import com.geopista.app.eiel.beans.InstalacionesDeportivasEIEL;
import com.geopista.app.eiel.beans.LonjasMercadosEIEL;
import com.geopista.app.eiel.beans.MataderosEIEL;
import com.geopista.app.eiel.beans.NucleoEncuestado7EIEL;
import com.geopista.app.eiel.beans.NucleosAbandonadosEIEL;
import com.geopista.app.eiel.beans.NucleosPoblacionEIEL;
import com.geopista.app.eiel.beans.OtrosServMunicipalesEIEL;
import com.geopista.app.eiel.beans.PadronMunicipiosEIEL;
import com.geopista.app.eiel.beans.PadronNucleosEIEL;
import com.geopista.app.eiel.beans.ParquesJardinesEIEL;
import com.geopista.app.eiel.beans.PlaneamientoUrbanoEIEL;
import com.geopista.app.eiel.beans.PoblamientoEIEL;
import com.geopista.app.eiel.beans.PuntosVertidoEIEL;
import com.geopista.app.eiel.beans.RecogidaBasurasEIEL;
import com.geopista.app.eiel.beans.SaneamientoAutonomoEIEL;
import com.geopista.app.eiel.beans.ServiciosAbastecimientosEIEL;
import com.geopista.app.eiel.beans.ServiciosRecogidaBasuraEIEL;
import com.geopista.app.eiel.beans.ServiciosSaneamientoEIEL;
import com.geopista.app.eiel.beans.TanatoriosEIEL;
import com.geopista.app.eiel.beans.TramosCarreterasEIEL;
import com.geopista.app.eiel.beans.TramosConduccionEIEL;
import com.geopista.app.eiel.beans.TratamientosPotabilizacionEIEL;
import com.geopista.app.eiel.beans.VertederosEIEL;
import com.geopista.app.eiel.models.AbastecimientoAutonomoCompletoEIELTableModel;
import com.geopista.app.eiel.models.CabildoConsejoCompletoEIELTableModel;
import com.geopista.app.eiel.models.CaptacionesCompletoEIELTableModel;
import com.geopista.app.eiel.models.CasasConsistorialesCompletoEIELTableModel;
import com.geopista.app.eiel.models.CementeriosCompletoEIELTableModel;
import com.geopista.app.eiel.models.CentrosAsistencialesCompletoEIELTableModel;
import com.geopista.app.eiel.models.CentrosCulturalesCompletoEIELTableModel;
import com.geopista.app.eiel.models.CentrosEnsenianzaCompletoEIELTableModel;
import com.geopista.app.eiel.models.CentrosSanitariosCompletoEIELTableModel;
import com.geopista.app.eiel.models.ColectoresCompletoEIELTableModel;
import com.geopista.app.eiel.models.DepositosCompletoEIELTableModel;
import com.geopista.app.eiel.models.Depuradora1CompletoEIELTableModel;
import com.geopista.app.eiel.models.Depuradora2CompletoEIELTableModel;
import com.geopista.app.eiel.models.DiseminadosCompletoEIELTableModel;
import com.geopista.app.eiel.models.EdificiosSinUsoCompletoEIELTableModel;
import com.geopista.app.eiel.models.EmisariosCompletoEIELTableModel;
import com.geopista.app.eiel.models.Encuestados1CompletoEIELTableModel;
import com.geopista.app.eiel.models.Encuestados2CompletoEIELTableModel;
import com.geopista.app.eiel.models.EntidadesAgrupadasCompletoEIELTableModel;
import com.geopista.app.eiel.models.EntidadesSingularesCompletoEIELTableModel;
import com.geopista.app.eiel.models.IncendiosProteccionCompletoEIELTableModel;
import com.geopista.app.eiel.models.InfoTerminosMunicipalesCompletoEIELTableModel;
import com.geopista.app.eiel.models.InstalacionesDeportivasCompletoEIELTableModel;
import com.geopista.app.eiel.models.LonjasMercadosCompletoEIELTableModel;
import com.geopista.app.eiel.models.MataderosCompletoEIELTableModel;
import com.geopista.app.eiel.models.NucleosAbandonadosCompletoEIELTableModel;
import com.geopista.app.eiel.models.NucleosPoblacionCompletoEIELTableModel;
import com.geopista.app.eiel.models.OtrosServMunicipalesCompletoEIELTableModel;
import com.geopista.app.eiel.models.PadronMunicipiosCompletoEIELTableModel;
import com.geopista.app.eiel.models.PadronNucleosCompletoEIELTableModel;
import com.geopista.app.eiel.models.ParquesJardinesCompletoEIELTableModel;
import com.geopista.app.eiel.models.PlaneamientoUrbanoCompletoEIELTableModel;
import com.geopista.app.eiel.models.PoblamientoCompletoEIELTableModel;
import com.geopista.app.eiel.models.PuntosVertidoCompletoEIELTableModel;
import com.geopista.app.eiel.models.RecogidaBasurasCompletoEIELTableModel;
import com.geopista.app.eiel.models.SaneamientoAutonomoCompletoEIELTableModel;
import com.geopista.app.eiel.models.ServiciosAbastecimientosCompletoEIELTableModel;
import com.geopista.app.eiel.models.ServiciosRecogidaBasuraCompletoEIELTableModel;
import com.geopista.app.eiel.models.ServiciosSaneamientoCompletoEIELTableModel;
import com.geopista.app.eiel.models.TanatoriosCompletoEIELTableModel;
import com.geopista.app.eiel.models.TramosCarreterasCompletoEIELTableModel;
import com.geopista.app.eiel.models.TramosConduccionCompletoEIELTableModel;
import com.geopista.app.eiel.models.TratamientosPotabilizacionCompletoEIELTableModel;
import com.geopista.app.eiel.models.VertederosCompletoEIELTableModel;
import com.geopista.app.eiel.panels.BotoneraJPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.ListaDatosEIELJPanel;
import com.geopista.app.eiel.utils.ColorTableCellRenderer;
import com.geopista.app.eiel.utils.ExcelExporter;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.feature.GeopistaFeature;
import com.geopista.model.GeopistaLayer;
import com.geopista.server.administradorCartografia.Const;
import com.geopista.util.config.UserPreferenceStore;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.model.FeatureEvent;
import com.vividsolutions.jump.workbench.model.FeatureEventType;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;
 


/**
 * @author seilagamo
 *
 */
public class EIELListaTablaDialog extends JDialog {
    
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Dimension d = tk.getScreenSize();
    public static final int DIM_X = 700;
    public static final int DIM_Y = 650;
    private String tableModelCompleto = null;
    private BotoneraJPanel jPanelBotonera = null;
    private JTable tablaDatos = null;  
    private TableModel tableListaDatosModel = null;
    private String patron = null;
    private ListaDatosEIELJPanel jPanelListaElementos = null;
    private JPanel panelPrincipal = null;
    
    public EIELListaTablaDialog(String tableModelCompleto, String patron, String titulo) throws HeadlessException {
        super(AppContext.getApplicationContext().getMainFrame());
        this.tableModelCompleto = tableModelCompleto;
        this.patron = patron;
        initialize(titulo);
    }
    
    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize(String title){
        Locale loc = I18N.getLocaleAsObject();  
        ResourceBundle bundle = ResourceBundle.getBundle(
                "com.geopista.app.eiel.language.LocalGISEIELi18n", loc, this.getClass()
                        .getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL", bundle);       

        this.setModal(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setSize(DIM_X, DIM_Y);
        int x = (d.width - DIM_X) / 2;  
        int y = (d.height - DIM_Y) / 2; 
        this.setLocation(x, y); 
        this.setTitle(title);
        this.setResizable(true);
        this.addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent e){
                dispose();
            }
        });     
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout());
        this.setLayout(new GridBagLayout());
        getContentPane().add(panelPrincipal);

        getJPanelBotonera().getJButtonAniadir().setEnabled(true);
        getJPanelBotonera().getJButtonModificar().setEnabled(false);
        getJPanelBotonera().getJButtonConsultar().setEnabled(false);

        //TODO: Eliminarlo??
        getJPanelBotonera().getJButtonVersionado().setEnabled(false);
        getJPanelBotonera().getJButtonEliminar().setEnabled(false);
        getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
        getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(false);
        getJPanelBotonera().getJButtonDesactivar().setEnabled(false);
        getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
        getJPanelBotonera().getJButtonExportar().setEnabled(true);
        getJPanelBotonera().remove(getJPanelBotonera().getJButtonListarTabla());
        
        panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        add(panelPrincipal, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.95,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
        add(getJPanelBotonera(), new GridBagConstraints(0, 1, 1, 1, 0.1, 0.05,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));        
        listarDatosTabla();
        reColorearBloqueo(patron);

    }
    
    
    public BotoneraJPanel getJPanelBotonera(){
        
        if (jPanelBotonera == null){
            
            jPanelBotonera = new BotoneraJPanel();          
            jPanelBotonera.setEnabled(false);
            jPanelBotonera.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    botoneraJPanel_actionPerformed();
                }
            });

        }
        return jPanelBotonera;
    }
    
    public void botoneraJPanel_actionPerformed(){
        try{
            if (getJPanelBotonera().getBotonPressed() == null) return;           
            
            if (getJPanelBotonera().getBotonPressed().equalsIgnoreCase(ConstantesLocalGISEIEL.OPERACION_ELIMINAR)){
                String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(getSelectedElement(), patron);
                if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                    JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                            + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                    listarDatosTabla();
                    reColorearBloqueo(patron);
                    return;

                }
                else{
                    bloquearElemento(getSelectedElement(), patron, true);

                    //Esta seguro que desea eliminar el registro.
                    if (confirm(I18N.get("LocalGISEIEL","localgiseiel.register.tag2"), 
                            I18N.get("LocalGISEIEL","localgiseiel.register.tag3"))){

                        eliminarElemento(getSelectedElement(), patron);
                        listarDatosTabla();
                        reColorearBloqueo(patron);
                        getJPanelBotonera().setEnabled(false);
                    }
                }
                return;
            }
            abrirDialogo(patron, getJPanelBotonera().getBotonPressed());
            listarDatosTabla();
           
        }catch (Exception e){
            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
        }
        getJPanelBotonera().setBotonPressed(null);

    }
    
    public JTable getJTableListaDatos() {
        if (tablaDatos == null) {
            tablaDatos = new JTable();
            if (tableModelCompleto != null) {

                try {

                    tableListaDatosModel = (TableModel) Class.forName(this.tableModelCompleto)
                            .newInstance();

                    TableSorted tblSorted = new TableSorted(tableListaDatosModel);
                    tblSorted.setTableHeader(tablaDatos.getTableHeader());
                    tablaDatos.setModel(tblSorted);
                    tablaDatos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    tablaDatos.setCellSelectionEnabled(false);
                    tablaDatos.setColumnSelectionAllowed(false);
                    tablaDatos.setRowSelectionAllowed(true);
                    tablaDatos.getTableHeader().setReorderingAllowed(false);

                    ((TableSorted) tablaDatos.getModel()).getTableHeader().addFocusListener(
                            new java.awt.event.FocusAdapter() {

                                public void focusGained(java.awt.event.FocusEvent evt) {
                                	
                                }
                            });

                    ((TableSorted) tablaDatos.getModel()).getTableHeader().addMouseListener(
                            new java.awt.event.MouseAdapter() {

                                public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                                }
                            });
                    
                    tablaDatos.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                        	//clearSelection(versionJTable);
                            //bienesJTableMouseReleased();
                        }
                        public void mouseClicked(java.awt.event.MouseEvent evt){
                        	if(evt.getClickCount() == 2) {
                        		/*getBienSeleccionado();
                        		for (Iterator <ActionListener>i = actionListeners.iterator(); i.hasNext();) {
                                    ActionListener l = (ActionListener) i.next();
                                    l.actionPerformed(new ActionEvent(this, 0, DOBLE_CLICK));
                                }*/
                            }
                        }
                    });

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return tablaDatos;
    }
    
    private void setNameTableModel(String nameTableModel){
        this.tableModelCompleto = nameTableModel;
    }
    
    private void listarDatosTabla(){
    	
    	Object selectedItem = this.getSelectedElement();
    	        
       
        if (patron != null && !patron.equals("")){
        	
        	            
            if (patron.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);

                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO);
        
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO);
                ((AbastecimientoAutonomoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);                
                
            }
            //MARKED
            else if (patron.equals(ConstantesLocalGISEIEL.CAPTACIONES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CAPTACIONES_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CAPTACIONES);
                ((CaptacionesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);

                setNameTableModel(ConstantesLocalGISEIEL.DEPURADORAS1_MODEL_COMPLETO);

                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                

                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DEPURADORAS1);
                ((Depuradora1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.DEPURADORAS2_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DEPURADORAS2);
                ((Depuradora2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES);
                ((CasasConsistorialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_CULTURALES_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_CULTURALES);
                ((CentrosCulturalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA);
                ((CentrosEnsenianzaCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS);
                ((InstalacionesDeportivasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);              
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.NUCLEOS_POBLACION_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.NUCLEOS_POBLACION);
                ((NucleosPoblacionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES);
                ((OtrosServMunicipalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.PADRON_NUCLEOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PADRON_NUCLEOS);
                ((PadronNucleosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);               
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS);
                ((PadronMunicipiosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);               
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.PARQUES_JARDINES_MODEL_COMPLETO);
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PARQUES_JARDINES);
                ((ParquesJardinesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO);
                ((PlaneamientoUrbanoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);               
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.POBLAMIENTO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.POBLAMIENTO);
                ((PoblamientoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.RECOGIDA_BASURAS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.RECOGIDA_BASURAS);
                ((RecogidaBasurasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();           
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_SANITARIOS);
                ((CentrosSanitariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO);
                ((SaneamientoAutonomoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);               
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO);
                ((ServiciosSaneamientoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA);
                ((ServiciosRecogidaBasuraCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO);
                ((EdificiosSinUsoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TANATORIOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.TANATORIOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TANATORIOS);
                ((TanatoriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.VERTEDEROS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DATOS_VERTEDEROS);
                ((VertederosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CARRETERAS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();    
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS);
                ((TramosCarreterasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.DEPOSITOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DEPOSITOS);
                ((DepositosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);               
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.PUNTOS_VERTIDO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PUNTOS_VERTIDO);
                ((PuntosVertidoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);              
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS);
                ((ServiciosAbastecimientosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);               
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES);
                ((CentrosAsistencialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CABILDO)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CABILDO_CONSEJO_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CABILDO);
                ((CabildoConsejoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.CEMENTERIOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CEMENTERIOS);
                ((CementeriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES);
                ((EntidadesSingularesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO);
                         
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();                
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.NUCLEO_ENCT_7);
                ((InfoTerminosMunicipalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION);
                ((IncendiosProteccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.LONJAS_MERCADOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.LONJAS_MERCADOS);
                ((LonjasMercadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.MATADEROS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.MATADEROS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.MATADEROS);
                ((MataderosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION);
                ((TratamientosPotabilizacionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.DISEMINADOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DISEMINADOS);
                ((DiseminadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.ENCUESTADOS1_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ENCUESTADOS1);
                ((Encuestados1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.ENCUESTADOS2_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ENCUESTADOS2);
                ((Encuestados2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS);
                ((NucleosAbandonadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }        
			//Prueba de concepto Elementos sin informacion alfanumerica.
			//ALFANUMERICOS
			//- EMISARIOS
            else if (patron.equals(ConstantesLocalGISEIEL.EMISARIOS)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(true);

                setNameTableModel(ConstantesLocalGISEIEL.EMISARIOS_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.EMISARIOS);
                ((EmisariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }        
            else if (patron.equals(ConstantesLocalGISEIEL.TCONDUCCION)){
                
                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
                getJPanelBotonera().getJButtonModificar().setEnabled(false);
                getJPanelBotonera().getJButtonConsultar().setEnabled(false);                
                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(true);
                
                setNameTableModel(ConstantesLocalGISEIEL.TCONDUCCION_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TCONDUCCION);
                ((TramosConduccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }        
            else if (patron.equals(ConstantesLocalGISEIEL.TCOLECTOR)){
     
			     getJPanelBotonera().getJButtonAniadir().setEnabled(true);
			     getJPanelBotonera().getJButtonModificar().setEnabled(false);
	             getJPanelBotonera().getJButtonConsultar().setEnabled(false);			     
			     getJPanelBotonera().getJButtonVersionado().setEnabled(false);
			     getJPanelBotonera().getJButtonEliminar().setEnabled(false);
			     getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
                getJPanelBotonera().getJCheckBoxFiltroGeometrias().setEnabled(true);

			     setNameTableModel(ConstantesLocalGISEIEL.TCOLECTOR_MODEL_COMPLETO);
			     
			     panelPrincipal.remove(jPanelListaElementos);
			     jPanelListaElementos = null;
			     panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
			             GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
			     panelPrincipal.revalidate();
			     
			     ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TCOLECTOR);
			     ((ColectoresCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
			 }  
            else if (patron.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
                
//                getJPanelBotonera().getJButtonAniadir().setEnabled(true);
//                getJPanelBotonera().getJButtonModificar().setEnabled(false);
//                getJPanelBotonera().getJButtonVersionado().setEnabled(false);
//                getJPanelBotonera().getJButtonEliminar().setEnabled(false);
//                getJPanelBotonera().getJButtonFiltroInformes().setEnabled(false);
//                getJPanelBotonera().getJButtonDigitalizar().setEnabled(false);
            	//getJPanelBotonera().setEnabled(false);
                
                setNameTableModel(ConstantesLocalGISEIEL.AGRUPACIONES6000_MODEL_COMPLETO);
                
                panelPrincipal.remove(jPanelListaElementos);
                jPanelListaElementos = null;
                panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
                panelPrincipal.revalidate();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.AGRUPACIONES6000);
                ((EntidadesAgrupadasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(lst);
            }
            else{
                getJPanelBotonera().setEnabled(false);
            }
            
            
          //en cualquier caso, ordenamos todos los campos de la tabla
    		OrdenarTabla(((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()));
    		
    		
    		//Intentamos dejar de nuevo seleecionado el elemento que hemos obtenido al principio del método
    		if (selectedItem != null){
    			this.setSeletecdRowByItem(selectedItem);
    		}
            
        }
        
    }
    
    private void setSeletecdRowByItem(Object selectedItem) {
    	try{
    		JTable table = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos();
    		for(int i=0; i< table.getRowCount(); i++ ){   			
    			if (getObjectByRowAndSelectedItem(i,selectedItem) != null){
    				((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectionModel().setSelectionInterval(i, i);
    				return;
    			}
    		}
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void OrdenarTabla(TableSorted tableSorted) {
    	for(int i=0; i < tableSorted.getColumnCount(); i ++){
			tableSorted.setSortingStatus(i, 1);
		}
	}
    
    public Object getObjectByRowAndSelectedItem(int selectedRow, Object selectedObject){

    	try {
    		String patternSelected = this.patron;

    		if (patternSelected != null && !patternSelected.equals("")){

    			if (patternSelected.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

    				AbastecimientoAutonomoEIEL obj= ((AbastecimientoAutonomoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				AbastecimientoAutonomoEIEL compareObject = (AbastecimientoAutonomoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINENucleo().equals(compareObject.getCodINENucleo()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())
    										){
    					return obj;
    				} else{
    					return null;
    				}

    			}
    			//MARKED
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

    				CaptacionesEIEL obj= ((CaptacionesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CaptacionesEIEL compareObject = (CaptacionesEIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

    				Depuradora1EIEL obj= ((Depuradora1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Depuradora1EIEL compareObject = (Depuradora1EIEL) selectedObject;
    				if (obj.getClave().equals(compareObject.getClave()) &&
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

    				Depuradora2EIEL obj= ((Depuradora2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Depuradora2EIEL compareObject = (Depuradora2EIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

    				CasasConsistorialesEIEL obj= ((CasasConsistorialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CasasConsistorialesEIEL compareObject = (CasasConsistorialesEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}

    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

    				CentrosCulturalesEIEL obj= ((CentrosCulturalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosCulturalesEIEL compareObject = (CentrosCulturalesEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

    				CentrosEnsenianzaEIEL obj= ((CentrosEnsenianzaCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosEnsenianzaEIEL compareObject = (CentrosEnsenianzaEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

    				InstalacionesDeportivasEIEL obj= ((InstalacionesDeportivasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				InstalacionesDeportivasEIEL compareObject = (InstalacionesDeportivasEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrdenIdDeportes().equals(compareObject.getOrdenIdDeportes())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

    				NucleosPoblacionEIEL obj= ((NucleosPoblacionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				NucleosAbandonadosEIEL compareObject = (NucleosAbandonadosEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

    				OtrosServMunicipalesEIEL obj= ((OtrosServMunicipalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				OtrosServMunicipalesEIEL compareObject = (OtrosServMunicipalesEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

    				PadronNucleosEIEL obj= ((PadronNucleosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PadronNucleosEIEL compareObject = (PadronNucleosEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

    				PadronMunicipiosEIEL obj= ((PadronMunicipiosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PadronMunicipiosEIEL compareObject = (PadronMunicipiosEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

    				ParquesJardinesEIEL obj= ((ParquesJardinesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ParquesJardinesEIEL compareObject = (ParquesJardinesEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else {
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

    				PlaneamientoUrbanoEIEL obj= ((PlaneamientoUrbanoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PlaneamientoUrbanoEIEL compareObject = (PlaneamientoUrbanoEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) &&
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    							obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

    				PoblamientoEIEL obj= ((PoblamientoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PoblamientoEIEL compareObject = (PoblamientoEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

    				RecogidaBasurasEIEL obj= ((RecogidaBasurasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				RecogidaBasurasEIEL compareObject = (RecogidaBasurasEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())
    										){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

    				CentrosSanitariosEIEL obj= ((CentrosSanitariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosSanitariosEIEL compareObject = (CentrosSanitariosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

    				SaneamientoAutonomoEIEL obj= ((SaneamientoAutonomoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				SaneamientoAutonomoEIEL compareObject = (SaneamientoAutonomoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINENucleo().equals(compareObject.getCodINENucleo()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

    				ServiciosSaneamientoEIEL obj= ((ServiciosSaneamientoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ServiciosSaneamientoEIEL compareObject = (ServiciosSaneamientoEIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
//    			else if (patternSelected.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
//
//    				ServiciosRecogidaBasuraEIEL obj= ((ServiciosRecogidaBasuraCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
//    				ServiciosRecogidaBasuraEIEL compareObject = (ServiciosRecogidaBasuraEIEL) selectedObject;
//    				
//    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
//    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
//    							obj.getCodINENucleo().equals(compareObject.getCodINENucleo()) &&
//    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) ){
//    					return obj;
//    				} else{
//    					return null;
//    				}
//    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

    				EdificiosSinUsoEIEL obj= ((EdificiosSinUsoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EdificiosSinUsoEIEL compareObject = (EdificiosSinUsoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TANATORIOS)){

    				TanatoriosEIEL obj= ((TanatoriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TanatoriosEIEL compareObject = (TanatoriosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

    				VertederosEIEL obj= ((VertederosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				VertederosEIEL compareObject = (VertederosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    								obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

    				TramosCarreterasEIEL obj= ((TramosCarreterasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TramosCarreterasEIEL compareObject = (TramosCarreterasEIEL) selectedObject;

    				if (obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    						obj.getCodCarretera().equals(compareObject.getCodCarretera())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPOSITOS)){

    				DepositosEIEL obj= ((DepositosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				DepositosEIEL compareObject = (DepositosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
   							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
									obj.getOrdenDeposito().equals(compareObject.getOrdenDeposito())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
    			
    				PuntosVertidoEIEL obj= ((PuntosVertidoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PuntosVertidoEIEL compareObject = (PuntosVertidoEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    								obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){

    				ServiciosAbastecimientosEIEL obj= ((ServiciosAbastecimientosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ServiciosAbastecimientosEIEL compareObject = (ServiciosAbastecimientosEIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){

    				CentrosAsistencialesEIEL obj= ((CentrosAsistencialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CentrosAsistencialesEIEL compareObject = (CentrosAsistencialesEIEL) selectedObject;

    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrdenAsistencial().equals(compareObject.getOrdenAsistencial())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CABILDO)){

    				CabildoConsejoEIEL obj= ((CabildoConsejoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CabildoConsejoEIEL compareObject = (CabildoConsejoEIEL) selectedObject;
    				
    				if (obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    						obj.getCodIsla().equals(compareObject.getCodIsla())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){

    				CementeriosEIEL obj= ((CementeriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CementeriosEIEL compareObject = (CementeriosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){

    				EntidadesSingularesEIEL obj= ((EntidadesSingularesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EntidadesSingularesEIEL compareObject = (EntidadesSingularesEIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){

    				NucleoEncuestado7EIEL obj= ((InfoTerminosMunicipalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				NucleoEncuestado7EIEL compareObject = (NucleoEncuestado7EIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){

    				IncendiosProteccionEIEL obj= ((IncendiosProteccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				IncendiosProteccionEIEL compareObject = (IncendiosProteccionEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){

    				LonjasMercadosEIEL obj= ((LonjasMercadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				LonjasMercadosEIEL compareObject = (LonjasMercadosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.MATADEROS)){

    				MataderosEIEL obj= ((MataderosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				MataderosEIEL compareObject = (MataderosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getOrden().equals(compareObject.getOrden())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){

    				TratamientosPotabilizacionEIEL obj= ((TratamientosPotabilizacionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TratamientosPotabilizacionEIEL compareObject = (TratamientosPotabilizacionEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) &&
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    								obj.getOrdenPotabilizadora().equals(compareObject.getOrdenPotabilizadora())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DISEMINADOS)){

    				DiseminadosEIEL obj= ((DiseminadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				DiseminadosEIEL compareObject = (DiseminadosEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) ){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){

    				Encuestados1EIEL obj= ((Encuestados1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Encuestados1EIEL compareObject = (Encuestados1EIEL) selectedObject;
    				
    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){

    				Encuestados2EIEL obj= ((Encuestados2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				Encuestados2EIEL compareObject = (Encuestados2EIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
  							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
   								obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
   									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    				
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){

    				NucleosAbandonadosEIEL obj= ((NucleosAbandonadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				NucleosAbandonadosEIEL compareObject = (NucleosAbandonadosEIEL) selectedObject;

    				if (obj.getCodINEEntidad().equals(compareObject.getCodINEEntidad()) && 
    						obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEPoblamiento().equals(compareObject.getCodINEPoblamiento()) &&
    								obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			//Prueba de concepto Elementos sin informacion alfanumerica.
    			//ALFANUMERICOS
    			//- EMISARIOS
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.EMISARIOS)){

    				EmisariosEIEL obj= ((EmisariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EmisariosEIEL compareObject = (EmisariosEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TCONDUCCION)){

    				TramosConduccionEIEL obj= ((TramosConduccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TramosConduccionEIEL compareObject = (TramosConduccionEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getTramo_cn().equals(compareObject.getTramo_cn())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

    				ColectorEIEL obj= ((ColectoresCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				ColectorEIEL compareObject = (ColectorEIEL) selectedObject;
    				
    				if (obj.getClave().equals(compareObject.getClave()) && 
    							obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    									obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    										obj.getCodOrden().equals(compareObject.getCodOrden())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){

    				EntidadesAgrupadasEIEL obj= ((EntidadesAgrupadasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				EntidadesAgrupadasEIEL compareObject = (EntidadesAgrupadasEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    																obj.getCodEntidad().equals(compareObject.getCodEntidad())
    										&& obj.getCodEntidad_agrupada().equals(compareObject.getCodEntidad_agrupada())
    										&& obj.getCodNucleo().equals(compareObject.getCodEntidad())
    										&& obj.getCodNucleo_agrupado().equals(compareObject.getCodEntidad_agrupada())){
    					return obj;
    				} else{
    					return null;
    				}
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return null;

    }
    
    
	private void reColorearBloqueo(String selectedPattern){

        Vector redRows= new Vector();

        for (int i=0; i < ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getRowCount(); i++){

            if (selectedPattern.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                AbastecimientoAutonomoEIEL elemento = ((AbastecimientoAutonomoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((AbastecimientoAutonomoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            //MARKED
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CaptacionesEIEL elemento = ((CaptacionesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CaptacionesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }

            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                Depuradora1EIEL elemento = ((Depuradora1CompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((Depuradora1CompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                Depuradora2EIEL elemento = ((Depuradora2CompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((Depuradora2CompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CasasConsistorialesEIEL elemento = ((CasasConsistorialesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CasasConsistorialesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CentrosCulturalesEIEL elemento = ((CentrosCulturalesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CentrosCulturalesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CentrosEnsenianzaEIEL elemento = ((CentrosEnsenianzaCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CentrosEnsenianzaCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                InstalacionesDeportivasEIEL elemento = ((InstalacionesDeportivasCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((InstalacionesDeportivasCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                NucleosPoblacionEIEL elemento = ((NucleosPoblacionCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((NucleosPoblacionCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                OtrosServMunicipalesEIEL elemento = ((OtrosServMunicipalesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((OtrosServMunicipalesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                PadronNucleosEIEL elemento = ((PadronNucleosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((PadronNucleosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                PadronMunicipiosEIEL elemento = ((PadronMunicipiosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((PadronMunicipiosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                ParquesJardinesEIEL elemento = ((ParquesJardinesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((ParquesJardinesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                PlaneamientoUrbanoEIEL elemento = ((PlaneamientoUrbanoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((PlaneamientoUrbanoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                PoblamientoEIEL elemento = ((PoblamientoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((PoblamientoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                RecogidaBasurasEIEL elemento = ((RecogidaBasurasCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((RecogidaBasurasCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CentrosSanitariosEIEL elemento = ((CentrosSanitariosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CentrosSanitariosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                SaneamientoAutonomoEIEL elemento = ((SaneamientoAutonomoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((SaneamientoAutonomoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                ServiciosSaneamientoEIEL elemento = ((ServiciosSaneamientoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((ServiciosSaneamientoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                ServiciosRecogidaBasuraEIEL elemento = ((ServiciosRecogidaBasuraCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((ServiciosRecogidaBasuraCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                EdificiosSinUsoEIEL elemento = ((EdificiosSinUsoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((EdificiosSinUsoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.TANATORIOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                TanatoriosEIEL elemento = ((TanatoriosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((TanatoriosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                VertederosEIEL elemento = ((VertederosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((VertederosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }else if (selectedPattern.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                TramosCarreterasEIEL elemento = ((TramosCarreterasCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((TramosCarreterasCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPOSITOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                DepositosEIEL elemento = ((DepositosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((DepositosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                PuntosVertidoEIEL elemento = ((PuntosVertidoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((PuntosVertidoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                ServiciosAbastecimientosEIEL elemento = ((ServiciosAbastecimientosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((ServiciosAbastecimientosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CentrosAsistencialesEIEL elemento = ((CentrosAsistencialesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CentrosAsistencialesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CABILDO)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CabildoConsejoEIEL elemento = ((CabildoConsejoCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CabildoConsejoCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                CementeriosEIEL elemento = ((CementeriosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((CementeriosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                EntidadesSingularesEIEL elemento = ((EntidadesSingularesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((EntidadesSingularesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                NucleoEncuestado7EIEL elemento = ((InfoTerminosMunicipalesCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((InfoTerminosMunicipalesCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                IncendiosProteccionEIEL elemento = ((IncendiosProteccionCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((IncendiosProteccionCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                LonjasMercadosEIEL elemento = ((LonjasMercadosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((LonjasMercadosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.MATADEROS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                MataderosEIEL elemento = ((MataderosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((MataderosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                TratamientosPotabilizacionEIEL elemento = ((TratamientosPotabilizacionCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((TratamientosPotabilizacionCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DISEMINADOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                DiseminadosEIEL elemento = ((DiseminadosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((DiseminadosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                Encuestados1EIEL elemento = ((Encuestados1CompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((Encuestados1CompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                Encuestados2EIEL elemento = ((Encuestados2CompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((Encuestados2CompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                NucleosAbandonadosEIEL elemento = ((NucleosAbandonadosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((NucleosAbandonadosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            }
			//Prueba de concepto Elementos sin informacion alfanumerica.
			//ALFANUMERICOS
			//- EMISARIOS
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.EMISARIOS)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                EmisariosEIEL elemento = ((EmisariosCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((EmisariosCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }

            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCONDUCCION)){
	
	            TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
	            TramosConduccionEIEL elemento = ((TramosConduccionCompletoEIELTableModel)tableModel).getValueAt(i);
	            if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
	                redRows.add(new Integer(i));
	            }else redRows.add(null);
	
	            for ( int j=0; j < ((TramosConduccionCompletoEIELTableModel)tableModel).getColumnCount(); j++){
	                TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
	                ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
	                col.setCellRenderer(colorTableCellRenderer);
	            }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

                TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                ColectorEIEL elemento = ((ColectoresCompletoEIELTableModel)tableModel).getValueAt(i);
                if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                    redRows.add(new Integer(i));
                }else redRows.add(null);

                for ( int j=0; j < ((ColectoresCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                    TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                    ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                    col.setCellRenderer(colorTableCellRenderer);
                }
            
            } else if (selectedPattern.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){

                    TableModel tableModel = ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel();
                    EntidadesAgrupadasEIEL elemento = ((EntidadesAgrupadasCompletoEIELTableModel)tableModel).getValueAt(i);
                    if (elemento != null && elemento.getBloqueado()!=null && !elemento.getBloqueado().equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,null,false))){
                        redRows.add(new Integer(i));
                    }else redRows.add(null);

                    for ( int j=0; j < ((EntidadesAgrupadasCompletoEIELTableModel)tableModel).getColumnCount(); j++){
                        TableColumn col= ((ListaDatosEIELJPanel)getJPanelListaElementos()).getJTableListaDatos().getColumnModel().getColumn(j);
                        ColorTableCellRenderer colorTableCellRenderer= new ColorTableCellRenderer(redRows);
                        col.setCellRenderer(colorTableCellRenderer);
                    }
            }
        }
    }
    
    private JPanel getJPanelListaElementos(){
        
        if (jPanelListaElementos == null){
            
            jPanelListaElementos  = new ListaDatosEIELJPanel(tableModelCompleto);     
            jPanelListaElementos.getJTableListaDatos().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jPanelListaElementos.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    listaelementosJPanel_actionPerformed();
                }
            });
        }
        return jPanelListaElementos;
    }
    
    private void listaelementosJPanel_actionPerformed(){
        /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */

        try {
            
            int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
        	
            if (patron != null && !patron.equals("") && selectedRow>=0){
                
                ((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(true);
                ((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(true);
                ((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(true);
                ((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(true);
//                ((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(true);
                
                //MARKED
                if (patron.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

                    CaptacionesEIEL obj= ((CaptacionesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
                    }
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){
                	Depuradora1EIEL obj= ((Depuradora1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                	
                	String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
    						}
    				}
    				
                	if (GeopistaEditorPanel.getEditor()!= null){
                		Collection lstFeatures;
                		lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                		refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                	}
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

                    Depuradora2EIEL obj= ((Depuradora2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                    }
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){
                    
                    VertederosEIEL obj= ((VertederosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    						
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
//    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);

    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.VERTEDERO_LAYER);
                    }
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
                	
                	DepositosEIEL obj= ((DepositosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                	
                	String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
//    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);

    						}
    				}
                	
                	if (GeopistaEditorPanel.getEditor()!= null){
                		Collection lstFeatures;
                		lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                		refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
                	}
                	
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
                    
                    PuntosVertidoEIEL obj= ((PuntosVertidoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER);
                    }                  
                }
                if (patron.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

                    CentrosSanitariosEIEL obj= ((CentrosSanitariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);

    						}
    					
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
                    }
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){
                    
                    CasasConsistorialesEIEL obj= ((CasasConsistorialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){
                    
                    CentrosCulturalesEIEL obj= ((CentrosCulturalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){
                    
                    CentrosEnsenianzaEIEL obj= ((CentrosEnsenianzaCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){
                    
                    InstalacionesDeportivasEIEL obj= ((InstalacionesDeportivasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){
                    
                    ParquesJardinesEIEL obj= ((ParquesJardinesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){
                    
                    EdificiosSinUsoEIEL obj= ((EdificiosSinUsoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TANATORIOS)){
                    
                    TanatoriosEIEL obj= ((TanatoriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.TANATORIO_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){
                    
                    TramosCarreterasEIEL obj= ((TramosCarreterasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(String.valueOf(AppContext.getIdMunicipio()))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CARRETERA_LAYER);
                    }                  
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
                    
                    CentrosAsistencialesEIEL obj= ((CentrosAsistencialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    
                    String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
    				if (obj != null){
    					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
    						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    							    							
    							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
    			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
    						}
    				}
                    
                    if (GeopistaEditorPanel.getEditor()!= null){
                        Collection lstFeatures;
                        lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                        refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
                    }                  
                }
                
    			//Prueba de concepto Elementos sin informacion alfanumerica.
    			//ALFANUMERICOS
    			//- EMISARIOS
            
            else if (patron.equals(ConstantesLocalGISEIEL.EMISARIOS)){
             
                EmisariosEIEL obj= ((EmisariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                
                String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
				if (obj != null){
					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    														
							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
						}
				}
                
                if (GeopistaEditorPanel.getEditor()!= null){
                    Collection lstFeatures;
                    lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                    refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.EMISARIOS_LAYER);
                }
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TCONDUCCION)){
                
                TramosConduccionEIEL obj= ((TramosConduccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                
                String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
				if (obj != null){
					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    														
							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
						}
				}
                
                if (GeopistaEditorPanel.getEditor()!= null){
                    Collection lstFeatures;
                    lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                    refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
                }
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TCOLECTOR)){
                
                ColectorEIEL obj= ((ColectoresCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                
                String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
				if (obj != null){
					if (!obj.getCodINEProvincia().equals(idMunicipio.substring(0,2))
						|| !obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    														
							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
						}
				}
                
                if (GeopistaEditorPanel.getEditor()!= null){
                    Collection lstFeatures;
                    lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(obj, patron);
                    refreshFeatureSelection(lstFeatures!=null?lstFeatures.toArray():null, ConstantesLocalGISEIEL.COLECTOR_LAYER);
                }
            }
            else if (patron.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
                
                EntidadesAgrupadasEIEL obj= ((EntidadesAgrupadasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                
                String idMunicipio = ConstantesLocalGISEIEL.idMunicipio;
				if (obj != null){
					((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(true);
					((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(true);
					((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(true);
	    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(true);

					if (!obj.getCodINEMunicipio().equals(idMunicipio.substring(2, 5))){
							((BotoneraJPanel)getJPanelBotonera()).getJButtonModificar().setEnabled(false);
							((BotoneraJPanel)getJPanelBotonera()).getJButtonConsultar().setEnabled(false);    														
							((BotoneraJPanel)getJPanelBotonera()).getJButtonVersionado().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonEliminar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonDigitalizar().setEnabled(false);
			    			((BotoneraJPanel)getJPanelBotonera()).getJCheckBoxFiltroGeometrias().setEnabled(false);
//			    			((BotoneraJPanel)getJPanelBotonera()).getJButtonInformes().setEnabled(false);
						}
				}

            }
           }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void refreshFeatureSelection(Object[] featuresId, String nameLayer) {
        try {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            GeopistaEditorPanel.getEditor().getSelectionManager().clear();

            GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel.getEditor()
                    .getLayerManager().getLayer(nameLayer);
            if (geopistaLayer==null)
            	return;
            Iterator allFeatures = geopistaLayer.getFeatureCollectionWrapper().getFeatures()
                    .iterator();
            while (allFeatures.hasNext()) {
                Feature feature = (Feature) allFeatures.next();
                for (int i = 0; i < featuresId.length; i++) {
                    Integer featureID = (Integer) featuresId[i];
                    if (((GeopistaFeature) feature).getSystemId().equalsIgnoreCase(
                            featureID.toString())) {
                        GeopistaEditorPanel.getEditor().select(geopistaLayer, feature);
                        break;
                    }
                }
            }

            GeopistaEditorPanel.getEditor().zoomToSelected();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

    }
    
    
    public Object getSelectedElement(){

        try {            
            
        	int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
        	
            if (patron != null && !patron.equals("") && selectedRow>= 0){

                if (patron.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){


                    AbastecimientoAutonomoEIEL obj= ((AbastecimientoAutonomoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;

                }
                //MARKED
                else if (patron.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

                    CaptacionesEIEL obj= ((CaptacionesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

                    Depuradora1EIEL obj= ((Depuradora1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

                    Depuradora2EIEL obj= ((Depuradora2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

                    CasasConsistorialesEIEL obj= ((CasasConsistorialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

                    CentrosCulturalesEIEL obj= ((CentrosCulturalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

                    CentrosEnsenianzaEIEL obj= ((CentrosEnsenianzaCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

                    InstalacionesDeportivasEIEL obj= ((InstalacionesDeportivasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

                    NucleosPoblacionEIEL obj= ((NucleosPoblacionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

                    OtrosServMunicipalesEIEL obj= ((OtrosServMunicipalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

                    PadronNucleosEIEL obj= ((PadronNucleosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

                    PadronMunicipiosEIEL obj= ((PadronMunicipiosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

                    ParquesJardinesEIEL obj= ((ParquesJardinesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

                    PlaneamientoUrbanoEIEL obj= ((PlaneamientoUrbanoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

                    PoblamientoEIEL obj= ((PoblamientoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

                    RecogidaBasurasEIEL obj= ((RecogidaBasurasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

                    CentrosSanitariosEIEL obj= ((CentrosSanitariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

                    SaneamientoAutonomoEIEL obj= ((SaneamientoAutonomoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

                    ServiciosSaneamientoEIEL obj= ((ServiciosSaneamientoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){

                    ServiciosRecogidaBasuraEIEL obj= ((ServiciosRecogidaBasuraCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

                    EdificiosSinUsoEIEL obj= ((EdificiosSinUsoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TANATORIOS)){

                    TanatoriosEIEL obj= ((TanatoriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

                    VertederosEIEL obj= ((VertederosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

                    TramosCarreterasEIEL obj= ((TramosCarreterasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
                    DepositosEIEL obj= ((DepositosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){

                    PuntosVertidoEIEL obj= ((PuntosVertidoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
                    
                    ServiciosAbastecimientosEIEL obj= ((ServiciosAbastecimientosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
                    
                    CentrosAsistencialesEIEL obj= ((CentrosAsistencialesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CABILDO)){
                    
                    CabildoConsejoEIEL obj= ((CabildoConsejoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){
                    
                    CementeriosEIEL obj= ((CementeriosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
                    
                    EntidadesSingularesEIEL obj= ((EntidadesSingularesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
                    
                    NucleoEncuestado7EIEL obj= ((InfoTerminosMunicipalesCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
                    
                    IncendiosProteccionEIEL obj= ((IncendiosProteccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
                    
                    LonjasMercadosEIEL obj= ((LonjasMercadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.MATADEROS)){
                    
                    MataderosEIEL obj= ((MataderosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
                    
                    TratamientosPotabilizacionEIEL obj= ((TratamientosPotabilizacionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
                    
                    DiseminadosEIEL obj= ((DiseminadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
                    
                    Encuestados1EIEL obj= ((Encuestados1CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
                    
                    Encuestados2EIEL obj= ((Encuestados2CompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
                    
                    NucleosAbandonadosEIEL obj= ((NucleosAbandonadosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }                
    			//Prueba de concepto Elementos sin informacion alfanumerica.
    			//ALFANUMERICOS
    			//- EMISARIOS
                else if (patron.equals(ConstantesLocalGISEIEL.EMISARIOS)){

                    EmisariosEIEL obj= ((EmisariosCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TCONDUCCION)){

                    TramosConduccionEIEL obj= ((TramosConduccionCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

                    ColectorEIEL obj= ((ColectoresCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){

                    EntidadesAgrupadasEIEL obj= ((EntidadesAgrupadasCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
   

    
    private void eliminarElemento(Object object, String tipoElemento){
        
        Collection lstFeatures = null;

        try {
            if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

             //MARKED   
            }else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CAPTACIONES)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPURADORAS1)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPURADORAS2)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PARQUES_JARDINES)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.POBLAMIENTO)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TANATORIOS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TANATORIO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.VERTEDERO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }           
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CARRETERA_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPOSITOS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CABILDO)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, "", tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.CEMENTERIOS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.MATADEROS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MATADEROS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.DISEMINADOS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ENCUESTADOS1)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.ENCUESTADOS2)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);
            }
            
			//Prueba de concepto Elementos sin informacion alfanumerica.
			//ALFANUMERICOS
			//- EMISARIOS
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.EMISARIOS)){
                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TCONDUCCION)){
		                
                lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.TCOLECTOR)){
     
			     lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
			     GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
			     String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
			     InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

            }
            else if (tipoElemento.equalsIgnoreCase(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
                
//			     lstFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, tipoElemento);
//			     GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.AGRUPACIONES6000_LAYER);
//			     String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
            	 String idLayer = null;
			     InitEIEL.clienteLocalGISEIEL.eliminarElemento(object, lstFeatures, idLayer, tipoElemento);

           }
            if (lstFeatures!=null)
                updateFeatures(lstFeatures.toArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    private void abrirDialogo(String selectedPattern, String tipoOperacion) throws Exception{

    	/*if(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_FILTRO_INFORMES)){

    		//Revisamos si hay seleccionado un elemento en el panel o si el usuario ha
    		//seleccionado features en el mapa. Con esto generamos un sql
    		HashMap<String,String> listaFiltros=new HashMap<String,String>();
    		if (!selectedPattern.equals(ConstantesLocalGISEIEL.PATRON_FICHA_MUNICIPAL)){
	    		//Object elementoSeleccionado=getSelectedElement();
	    		listaFiltros=new FiltroByFeature().generateSQLFilterFeaturesSeleccionadas(selectedPattern,selectedItem,locale);
    		}	    		
	    		
	    		//Le pasamos las tres posibilidades de filtro que hay
	    		//1. Que el usuario haya seleccionado solo el epigrafe
	    		//2. Que el usuario haya seleccionado un elemento en el listado de elemento de un epigrafe
	    		//3. Que el usario seleccionad una zona del mapa en la que hay features.
	    		FichasFilterDialog dialog = new FichasFilterDialog(aplicacion.getMainFrame(),selectedPattern,listaFiltros,locale);
				dialog.addActionListener(new java.awt.event.ActionListener(){
		            public void actionPerformed(ActionEvent e){
		            	dialog_actionPerformed((FichasFilterDialog)e.getSource());
		            }
		        });
				dialog.setVisible(true);
				
    	}*/
    	
    	if(tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_EXPORTAR)){
    		ExcelExporter exporter=new ExcelExporter();
    		JTable table=((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos();
    		exporter.exportTable(table,this);
    		//Object tableModel=(Object)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel();
    	}
    	
   	
        if (selectedPattern.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(true);
                dialog.setVisible(true);
                if (dialog.getAbastecimientoAutonomoPanel().getOkPressed()){

                    AbastecimientoAutonomoEIEL abst = dialog.getAbastecimientoAutonomo(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof AbastecimientoAutonomoEIEL){

                    AbastecimientoAutonomoEIEL selectedElement = (AbastecimientoAutonomoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(selectedElement,false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron, true);
                        AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getAbastecimientoAutonomoPanel().getOkPressed()){

                            AbastecimientoAutonomoEIEL abst = null;                     
                            abst = dialog.getAbastecimientoAutonomo(selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron, false);
                    }
                }
            } 
        }
        //MARKED
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                CaptacionesDialog dialog = new CaptacionesDialog(true);
                dialog.setVisible(true);
                if (dialog.getCaptacionesPanel().getOkPressed()){

                    CaptacionesEIEL abst = dialog.getCaptacion(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof CaptacionesEIEL){

                    CaptacionesEIEL selectedElement = (CaptacionesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        CaptacionesDialog dialog = new CaptacionesDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        CaptacionesDialog dialog = new CaptacionesDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getCaptacionesPanel().getOkPressed()){
                            CaptacionesEIEL abst =  dialog.getCaptacion((CaptacionesEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
           }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                Depuradoras1Dialog dialog = new Depuradoras1Dialog(true);
                dialog.setVisible(true);
                if (dialog.getDepuradoras1Panel().getOkPressed()){

                    Depuradora1EIEL abst = dialog.getDepuradora1(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof Depuradora1EIEL){
                    Depuradora1EIEL selectedElement = (Depuradora1EIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        Depuradoras1Dialog dialog = new Depuradoras1Dialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        Depuradoras1Dialog dialog = new Depuradoras1Dialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getDepuradoras1Panel().getOkPressed()){
                            Depuradora1EIEL abst =  dialog.getDepuradora1((Depuradora1EIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                Depuradoras2Dialog dialog = new Depuradoras2Dialog(true);
                dialog.setVisible(true);
                if (dialog.getDepuradoras2Panel().getOkPressed()){

                    Depuradora2EIEL abst = dialog.getDepuradora2(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof Depuradora2EIEL){
                    Depuradora2EIEL selectedElement = (Depuradora2EIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        Depuradoras2Dialog dialog = new Depuradoras2Dialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        Depuradoras2Dialog dialog = new Depuradoras2Dialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getDepuradoras2Panel().getOkPressed()){
                            Depuradora2EIEL abst = dialog.getDepuradora2((Depuradora2EIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }       
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                CasaConsistorialDialog dialog = new CasaConsistorialDialog(true);
                dialog.setVisible(true);
                if (dialog.getCasaConsistorialPanel().getOkPressed()){

                    CasasConsistorialesEIEL abst = dialog.getCasaConsistorial(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof CasasConsistorialesEIEL){
                    CasasConsistorialesEIEL selectedElement = (CasasConsistorialesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        CasaConsistorialDialog dialog = new CasaConsistorialDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        CasaConsistorialDialog dialog = new CasaConsistorialDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getCasaConsistorialPanel().getOkPressed()){
                            CasasConsistorialesEIEL abst = dialog.getCasaConsistorial((CasasConsistorialesEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }       
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                CentroCulturalDialog dialog = new CentroCulturalDialog(true);
                dialog.setVisible(true);
                if (dialog.getCentroCulturalPanel().getOkPressed()){

                    CentrosCulturalesEIEL abst = dialog.getCentroCultural(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosCulturalesEIEL){
                    CentrosCulturalesEIEL selectedElement = (CentrosCulturalesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        CentroCulturalDialog dialog = new CentroCulturalDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        CentroCulturalDialog dialog = new CentroCulturalDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getCentroCulturalPanel().getOkPressed()){
                            CentrosCulturalesEIEL abst = dialog.getCentroCultural((CentrosCulturalesEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }           
        }       
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(true);
                dialog.setVisible(true);
                if (dialog.getCentroEnsenianzaPanel().getOkPressed()){

                    CentrosEnsenianzaEIEL abst = dialog.getCentroEnsenianza(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosEnsenianzaEIEL){
                    CentrosEnsenianzaEIEL selectedElement = (CentrosEnsenianzaEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getCentroEnsenianzaPanel().getOkPressed()){
                            CentrosEnsenianzaEIEL abst = dialog.getCentroEnsenianza((CentrosEnsenianzaEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }       
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(true);
                dialog.setVisible(true);
                if (dialog.getInstalacionDeportivaPanel().getOkPressed()){

                    InstalacionesDeportivasEIEL abst = dialog.getInstalacionDeportiva(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof InstalacionesDeportivasEIEL){
                    InstalacionesDeportivasEIEL selectedElement = (InstalacionesDeportivasEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getInstalacionDeportivaPanel().getOkPressed()){
                            InstalacionesDeportivasEIEL abst = dialog.getInstalacionDeportiva((InstalacionesDeportivasEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }       
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(true);
                dialog.setVisible(true);
                if (dialog.getNucleosPoblacionPanel().getOkPressed()){

                    NucleosPoblacionEIEL abst = dialog.getNucleosPoblacion(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof NucleosPoblacionEIEL){
                    NucleosPoblacionEIEL selectedElement = (NucleosPoblacionEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getNucleosPoblacionPanel().getOkPressed()){
                            NucleosPoblacionEIEL abst = dialog.getNucleosPoblacion((NucleosPoblacionEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(true);
                dialog.setVisible(true);
                if (dialog.getOtrosServMunicipalesPanel().getOkPressed()){

                    OtrosServMunicipalesEIEL abst = dialog.getOtrosServMunicipales(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof OtrosServMunicipalesEIEL){
                    OtrosServMunicipalesEIEL selectedElement = (OtrosServMunicipalesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getOtrosServMunicipalesPanel().getOkPressed()){
                            OtrosServMunicipalesEIEL abst = dialog.getOtrosServMunicipales((OtrosServMunicipalesEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }       
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                PadronNucleosDialog dialog = new PadronNucleosDialog(true);
                dialog.setVisible(true);
                if (dialog.getPadronNucleosPanel().getOkPressed()){

                    PadronNucleosEIEL abst = dialog.getPadronNucleos(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof PadronNucleosEIEL){
                    PadronNucleosEIEL selectedElement = (PadronNucleosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        PadronNucleosDialog dialog = new PadronNucleosDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        PadronNucleosDialog dialog = new PadronNucleosDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getPadronNucleosPanel().getOkPressed()){
                            PadronNucleosEIEL abst = dialog.getPadronNucleos((PadronNucleosEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
           
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(true);
                dialog.setVisible(true);
                if (dialog.getPadronMunicipiosPanel().getOkPressed()){

                    PadronMunicipiosEIEL abst = dialog.getPadronMunicipios(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof PadronMunicipiosEIEL){
                    PadronMunicipiosEIEL selectedElement = (PadronMunicipiosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getPadronMunicipiosPanel().getOkPressed()){
                            PadronMunicipiosEIEL abst = dialog.getPadronMunicipios((PadronMunicipiosEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                ParquesJardinesDialog dialog = new ParquesJardinesDialog(true);
                dialog.setVisible(true);
                if (dialog.getParquesJardinesPanel().getOkPressed()){

                    ParquesJardinesEIEL abst = dialog.getParquesJardines(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof ParquesJardinesEIEL){
                    ParquesJardinesEIEL selectedElement = (ParquesJardinesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        ParquesJardinesDialog dialog = new ParquesJardinesDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        ParquesJardinesDialog dialog = new ParquesJardinesDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getParquesJardinesPanel().getOkPressed()){
                            ParquesJardinesEIEL abst = dialog.getParquesJardines((ParquesJardinesEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(true);
                dialog.setVisible(true);
                if (dialog.getPlaneamientoUrbanoPanel().getOkPressed()){

                    PlaneamientoUrbanoEIEL abst = dialog.getPlaneamientoUrbano(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof PlaneamientoUrbanoEIEL){
                    PlaneamientoUrbanoEIEL selectedElement = (PlaneamientoUrbanoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getPlaneamientoUrbanoPanel().getOkPressed()){
                            PlaneamientoUrbanoEIEL abst = dialog.getPlaneamientoUrbano((PlaneamientoUrbanoEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
           
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                PoblamientoDialog dialog = new PoblamientoDialog(true);
                dialog.setVisible(true);
                if (dialog.getPoblamientoPanel().getOkPressed()){

                    PoblamientoEIEL abst = dialog.getPoblamiento(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof PoblamientoEIEL){
                    PoblamientoEIEL selectedElement = (PoblamientoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        PoblamientoDialog dialog = new PoblamientoDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        PoblamientoDialog dialog = new PoblamientoDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getPoblamientoPanel().getOkPressed()){
                            PoblamientoEIEL abst = dialog.getPoblamiento((PoblamientoEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(true);
                dialog.setVisible(true);
                if (dialog.getRecogidaBasurasPanel().getOkPressed()){

                    RecogidaBasurasEIEL abst = dialog.getRecogidaBasuras(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof RecogidaBasurasEIEL){
                    RecogidaBasurasEIEL selectedElement = (RecogidaBasurasEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getRecogidaBasurasPanel().getOkPressed()){
                            RecogidaBasurasEIEL abst = dialog.getRecogidaBasuras((RecogidaBasurasEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(true);
                dialog.setVisible(true);
                if (dialog.getCentrosSanitariosPanel().getOkPressed()){

                    CentrosSanitariosEIEL abst = dialog.getCentrosSanitarios(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosSanitariosEIEL){
                    CentrosSanitariosEIEL selectedElement = (CentrosSanitariosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getCentrosSanitariosPanel().getOkPressed()){
                            CentrosSanitariosEIEL abst = dialog.getCentrosSanitarios((CentrosSanitariosEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }           
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(true);
                dialog.setVisible(true);
                if (dialog.getSaneamientoAutonomoPanel().getOkPressed()){

                    SaneamientoAutonomoEIEL abst = dialog.getSaneamientoAutonomo(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof SaneamientoAutonomoEIEL){
                    SaneamientoAutonomoEIEL selectedElement = (SaneamientoAutonomoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getSaneamientoAutonomoPanel().getOkPressed()){
                            SaneamientoAutonomoEIEL abst = dialog.getSaneamientoAutonomo((SaneamientoAutonomoEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
            
        }
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(true);
                dialog.setVisible(true);
                if (dialog.getServiciosSaneamientoPanel().getOkPressed()){

                    ServiciosSaneamientoEIEL abst = dialog.getServiciosSaneamiento(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof ServiciosSaneamientoEIEL){
                    ServiciosSaneamientoEIEL selectedElement = (ServiciosSaneamientoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getServiciosSaneamientoPanel().getOkPressed()){
                            ServiciosSaneamientoEIEL abst = dialog.getServiciosSaneamiento((ServiciosSaneamientoEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){

                if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                    ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(true);
                    dialog.setVisible(true);
                    if (dialog.getServiciosRecogidaBasurasPanel().getOkPressed()){

                        ServiciosRecogidaBasuraEIEL abst = dialog.getServiciosRecogidaBasuras(null);
                        GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                        String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                        InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                    }
                }
                else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                    if (getSelectedElement()!= null && getSelectedElement() instanceof ServiciosRecogidaBasuraEIEL){
                        ServiciosRecogidaBasuraEIEL selectedElement = (ServiciosRecogidaBasuraEIEL) getSelectedElement();
                        String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                        if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                    + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                            ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(selectedElement, false);
                            dialog.setVisible(true);  

                        }
                        else{

                            bloquearElemento(selectedElement, patron,true);
                            ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(selectedElement,true);
                            dialog.setVisible(true);  

                            if (dialog.getServiciosRecogidaBasurasPanel().getOkPressed()){
                                ServiciosRecogidaBasuraEIEL abst = dialog.getServiciosRecogidaBasuras((ServiciosRecogidaBasuraEIEL) selectedElement);

                                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                            }
                            bloquearElemento(getSelectedElement(), patron,false);
                        }
                    }
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

                if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                    EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(true);
                    dialog.setVisible(true);
                    if (dialog.getEdificiosSinUsoPanel().getOkPressed()){

                        EdificiosSinUsoEIEL abst = dialog.getEdificiosSinUso(null);
                        GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
                        String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                        InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                    }
                }
                else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                    if (getSelectedElement()!= null && getSelectedElement() instanceof EdificiosSinUsoEIEL){
                        EdificiosSinUsoEIEL selectedElement = (EdificiosSinUsoEIEL) getSelectedElement();
                        String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                        if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                    + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                            EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(selectedElement, false);
                            dialog.setVisible(true);  

                        }
                        else{

                            bloquearElemento(selectedElement, patron,true);
                            EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(selectedElement,true);
                            dialog.setVisible(true);  

                            if (dialog.getEdificiosSinUsoPanel().getOkPressed()){
                                EdificiosSinUsoEIEL abst = dialog.getEdificiosSinUso((EdificiosSinUsoEIEL) selectedElement);

                                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
                                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                            }
                            bloquearElemento(getSelectedElement(), patron,false);
                        }
                    }
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.TANATORIOS)){

                if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                    TanatoriosDialog dialog = new TanatoriosDialog(true);
                    dialog.setVisible(true);
                    if (dialog.getTanatoriosPanel().getOkPressed()){

                        TanatoriosEIEL abst = dialog.getTanatorios(null);
                        GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TANATORIO_LAYER);
                        String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                        InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                    }
                }
                else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                    if (getSelectedElement()!= null && getSelectedElement() instanceof TanatoriosEIEL){
                        TanatoriosEIEL selectedElement = (TanatoriosEIEL) getSelectedElement();
                        String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                        if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                    + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                            TanatoriosDialog dialog = new TanatoriosDialog(selectedElement, false);
                            dialog.setVisible(true);  

                        }
                        else{

                            bloquearElemento(selectedElement, patron,true);
                            TanatoriosDialog dialog = new TanatoriosDialog(selectedElement,true);
                            dialog.setVisible(true);  

                            if (dialog.getTanatoriosPanel().getOkPressed()){
                                TanatoriosEIEL abst = dialog.getTanatorios((TanatoriosEIEL) selectedElement);

                                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TANATORIO_LAYER);
                                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                            }
                            bloquearElemento(getSelectedElement(), patron,false);
                        }
                    }
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

                if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                    VertederosDialog dialog = new VertederosDialog(true);
                    dialog.setVisible(true);
                    if (dialog.getVertederosPanel().getOkPressed()){

                        VertederosEIEL abst = dialog.getVertederos(null);
                        GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.VERTEDERO_LAYER);
                        String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                        InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                    }
                }
                else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                    if (getSelectedElement()!= null && getSelectedElement() instanceof VertederosEIEL){
                        VertederosEIEL selectedElement = (VertederosEIEL) getSelectedElement();
                        String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                        if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                    + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                            VertederosDialog dialog = new VertederosDialog(selectedElement, false);
                            dialog.setVisible(true);  

                        }
                        else{

                            bloquearElemento(selectedElement, patron,true);
                            VertederosDialog dialog = new VertederosDialog(selectedElement,true);
                            dialog.setVisible(true);  

                            if (dialog.getVertederosPanel().getOkPressed()){
                                VertederosEIEL abst = dialog.getVertederos((VertederosEIEL) selectedElement);

                                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.VERTEDERO_LAYER);
                                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                            }
                            bloquearElemento(getSelectedElement(), patron,false);
                        }
                    }
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

                if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                    CarreterasDialog dialog = new CarreterasDialog(true);
                    dialog.setVisible(true);
                    if (dialog.getCarreterasPanel().getOkPressed()){

                        TramosCarreterasEIEL abst = dialog.getCarreteras(null);
                        GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CARRETERA_LAYER);
                        String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                        InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                    }
                }
                else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                    if (getSelectedElement()!= null && getSelectedElement() instanceof TramosCarreterasEIEL){
                        TramosCarreterasEIEL selectedElement = (TramosCarreterasEIEL) getSelectedElement();
                        String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                        if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                    + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                            CarreterasDialog dialog = new CarreterasDialog(selectedElement, false);
                            dialog.setVisible(true);  

                        }
                        else{

                            bloquearElemento(selectedElement, patron,true);
                            CarreterasDialog dialog = new CarreterasDialog(selectedElement,true);
                            dialog.setVisible(true);  

                            if (dialog.getCarreterasPanel().getOkPressed()){
                                TramosCarreterasEIEL abst = dialog.getCarreteras((TramosCarreterasEIEL) selectedElement);

                                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CARRETERA_LAYER);
                                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                            }
                            bloquearElemento(getSelectedElement(), patron,false);
                        }
                    }
                }
            }
            else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                DepositosDialog dialog = new DepositosDialog(true);
                dialog.setVisible(true);
                if (dialog.getDepositosPanel().getOkPressed()){
                    DepositosEIEL abst = dialog.getDepositos(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof DepositosEIEL){
                    DepositosEIEL selectedElement = (DepositosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        DepositosDialog dialog = new DepositosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        DepositosDialog dialog = new DepositosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getDepositosPanel().getOkPressed()){
                            DepositosEIEL abst = dialog.getDepositos((DepositosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                PuntosVertidoDialog dialog = new PuntosVertidoDialog(true);
                dialog.setVisible(true);
                if (dialog.getPuntosVertidoPanel().getOkPressed()){
                    PuntosVertidoEIEL abst = dialog.getPuntosVertido(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PUNTOS_VERTIDO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof PuntosVertidoEIEL){
                    PuntosVertidoEIEL selectedElement = (PuntosVertidoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        PuntosVertidoDialog dialog = new PuntosVertidoDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        PuntosVertidoDialog dialog = new PuntosVertidoDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getPuntosVertidoPanel().getOkPressed()){
                            PuntosVertidoEIEL abst = dialog.getPuntosVertido((PuntosVertidoEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(true);
                dialog.setVisible(true);
                if (dialog.getServiciosAbastecimientosPanel().getOkPressed()){
                    ServiciosAbastecimientosEIEL abst = dialog.getServiciosAbastecimientos(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof ServiciosAbastecimientosEIEL){
                    ServiciosAbastecimientosEIEL selectedElement = (ServiciosAbastecimientosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getServiciosAbastecimientosPanel().getOkPressed()){
                            ServiciosAbastecimientosEIEL abst = dialog.getServiciosAbastecimientos((ServiciosAbastecimientosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(true);
                dialog.setVisible(true);
                if (dialog.getCentrosAsistencialesPanel().getOkPressed()){
                    CentrosAsistencialesEIEL abst = dialog.getCentrosAsistenciales(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof CentrosAsistencialesEIEL){
                    CentrosAsistencialesEIEL selectedElement = (CentrosAsistencialesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getCentrosAsistencialesPanel().getOkPressed()){
                            CentrosAsistencialesEIEL abst = dialog.getCentrosAsistenciales((CentrosAsistencialesEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CABILDO)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                CabildoConsejoDialog dialog = new CabildoConsejoDialog(true);
                dialog.setVisible(true);
                if (dialog.getCabildoConsejoPanel().getOkPressed()){
                    CabildoConsejoEIEL abst = dialog.getCabildoConsejo(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, "", selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof CabildoConsejoEIEL){
                    CabildoConsejoEIEL selectedElement = (CabildoConsejoEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        CabildoConsejoDialog dialog = new CabildoConsejoDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        CabildoConsejoDialog dialog = new CabildoConsejoDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getCabildoConsejoPanel().getOkPressed()){
                            CabildoConsejoEIEL abst = dialog.getCabildoConsejo((CabildoConsejoEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.PROVINCIA_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, "", selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                CementeriosDialog dialog = new CementeriosDialog(true);
                dialog.setVisible(true);
                if (dialog.getCementeriosPanel().getOkPressed()){
                    CementeriosEIEL abst = dialog.getCementerios(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof CementeriosEIEL){
                    CementeriosEIEL selectedElement = (CementeriosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        CementeriosDialog dialog = new CementeriosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        CementeriosDialog dialog = new CementeriosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getCementeriosPanel().getOkPressed()){
                            CementeriosEIEL abst = dialog.getCementerios((CementeriosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(true);
                dialog.setVisible(true);
                if (dialog.getEntidadesSingularesPanel().getOkPressed()){
                    EntidadesSingularesEIEL abst = dialog.getEntidadesSingulares(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof EntidadesSingularesEIEL){
                    EntidadesSingularesEIEL selectedElement = (EntidadesSingularesEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getEntidadesSingularesPanel().getOkPressed()){
                            EntidadesSingularesEIEL abst = dialog.getEntidadesSingulares((EntidadesSingularesEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
         }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(true);
                dialog.setVisible(true);
                if (dialog.getInfoTerminosMunicipalesPanel().getOkPressed()){
                    NucleoEncuestado7EIEL abst = dialog.getInfoTerminosMunicipales(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof NucleoEncuestado7EIEL){
                    NucleoEncuestado7EIEL selectedElement = (NucleoEncuestado7EIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getInfoTerminosMunicipalesPanel().getOkPressed()){
                            NucleoEncuestado7EIEL abst = dialog.getInfoTerminosMunicipales((NucleoEncuestado7EIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
          }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(true);
                dialog.setVisible(true);
                if (dialog.getIncendiosProteccionPanel().getOkPressed()){
                    IncendiosProteccionEIEL abst = dialog.getIncendiosProteccion(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof IncendiosProteccionEIEL){
                    IncendiosProteccionEIEL selectedElement = (IncendiosProteccionEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getIncendiosProteccionPanel().getOkPressed()){
                            IncendiosProteccionEIEL abst = dialog.getIncendiosProteccion((IncendiosProteccionEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                LonjasMercadosDialog dialog = new LonjasMercadosDialog(true);
                dialog.setVisible(true);
                if (dialog.getLonjasMercadosPanel().getOkPressed()){
                    LonjasMercadosEIEL abst = dialog.getLonjasMercados(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof LonjasMercadosEIEL){
                    LonjasMercadosEIEL selectedElement = (LonjasMercadosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        LonjasMercadosDialog dialog = new LonjasMercadosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        LonjasMercadosDialog dialog = new LonjasMercadosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getLonjasMercadosPanel().getOkPressed()){
                            LonjasMercadosEIEL abst = dialog.getLonjasMercados((LonjasMercadosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }

        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.MATADEROS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                MataderosDialog dialog = new MataderosDialog(true);
                dialog.setVisible(true);
                if (dialog.getMataderosPanel().getOkPressed()){
                    MataderosEIEL abst = dialog.getMataderos(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MATADEROS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof MataderosEIEL){
                    MataderosEIEL selectedElement = (MataderosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        MataderosDialog dialog = new MataderosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        MataderosDialog dialog = new MataderosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getMataderosPanel().getOkPressed()){
                            MataderosEIEL abst = dialog.getMataderos((MataderosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MATADEROS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }

        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(true);
                dialog.setVisible(true);
                if (dialog.getTratamientosPotabilizacionPanel().getOkPressed()){
                    TratamientosPotabilizacionEIEL abst = dialog.getTratamientosPotabilizacion(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof TratamientosPotabilizacionEIEL){
                    TratamientosPotabilizacionEIEL selectedElement = (TratamientosPotabilizacionEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getTratamientosPotabilizacionPanel().getOkPressed()){
                            TratamientosPotabilizacionEIEL abst = dialog.getTratamientosPotabilizacion((TratamientosPotabilizacionEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                DiseminadosDialog dialog = new DiseminadosDialog(true);
                dialog.setVisible(true);
                if (dialog.getDiseminadosPanel().getOkPressed()){
                    DiseminadosEIEL abst = dialog.getDiseminados(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof DiseminadosEIEL){
                    DiseminadosEIEL selectedElement = (DiseminadosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        DiseminadosDialog dialog = new DiseminadosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        DiseminadosDialog dialog = new DiseminadosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getDiseminadosPanel().getOkPressed()){
                            DiseminadosEIEL abst = dialog.getDiseminados((DiseminadosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                Encuestados1Dialog dialog = new Encuestados1Dialog(true);
                dialog.setVisible(true);
                if (dialog.getEncuestados1Panel().getOkPressed()){
                    Encuestados1EIEL abst = dialog.getEncuestados1(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof Encuestados1EIEL){
                    Encuestados1EIEL selectedElement = (Encuestados1EIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        Encuestados1Dialog dialog = new Encuestados1Dialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        Encuestados1Dialog dialog = new Encuestados1Dialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getEncuestados1Panel().getOkPressed()){
                            Encuestados1EIEL abst = dialog.getEncuestados1((Encuestados1EIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                Encuestados2Dialog dialog = new Encuestados2Dialog(true);
                dialog.setVisible(true);
                if (dialog.getEncuestados2Panel().getOkPressed()){
                    Encuestados2EIEL abst = dialog.getEncuestados2(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof Encuestados2EIEL){
                    Encuestados2EIEL selectedElement = (Encuestados2EIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        Encuestados2Dialog dialog = new Encuestados2Dialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        Encuestados2Dialog dialog = new Encuestados2Dialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getEncuestados2Panel().getOkPressed()){
                            Encuestados2EIEL abst = dialog.getEncuestados2((Encuestados2EIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
        }
        
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
                NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(true);
                dialog.setVisible(true);
                if (dialog.getNucleosAbandonadosPanel().getOkPressed()){
                    NucleosAbandonadosEIEL abst = dialog.getNucleosAbandonados(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            } else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
                if (getSelectedElement()!= null && getSelectedElement() instanceof NucleosAbandonadosEIEL){
                    NucleosAbandonadosEIEL selectedElement = (NucleosAbandonadosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()
                            !=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){
                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));
                        NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(selectedElement, false);
                        dialog.setVisible(true);  
                    } else{
                        bloquearElemento(selectedElement, patron,true);
                        NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(selectedElement,true);
                        dialog.setVisible(true);  
                        if (dialog.getNucleosAbandonadosPanel().getOkPressed()){
                            NucleosAbandonadosEIEL abst = dialog.getNucleosAbandonados((NucleosAbandonadosEIEL) selectedElement);
                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.NUCLEO_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
         }     
		//Prueba de concepto Elementos sin informacion alfanumerica.
		//ALFANUMERICOS
		//- EMISARIOS
        else if (selectedPattern.equals(ConstantesLocalGISEIEL.EMISARIOS)){

            if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                EmisoresDialog dialog = new EmisoresDialog(true);
                dialog.setVisible(true);
                if (dialog.getEmisoresPanel().getOkPressed()){

                    EmisariosEIEL abst = dialog.getEmisor(null);
                    GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
                    String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                    InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                }
            }
            else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                if (getSelectedElement()!= null && getSelectedElement() instanceof EmisariosEIEL){

                	EmisariosEIEL selectedElement = (EmisariosEIEL) getSelectedElement();
                    String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                    if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                        JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                        EmisoresDialog dialog = new EmisoresDialog(selectedElement, false);
                        dialog.setVisible(true);  

                    }
                    else{

                        bloquearElemento(selectedElement, patron,true);
                        EmisoresDialog dialog = new EmisoresDialog(selectedElement,true);
                        dialog.setVisible(true);  

                        if (dialog.getEmisoresPanel().getOkPressed()){
                            EmisariosEIEL abst =  dialog.getEmisor((EmisariosEIEL) selectedElement);

                            GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
                            String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                            InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                        }
                        bloquearElemento(getSelectedElement(), patron,false);
                    }
                }
            }
           }       
        	else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCONDUCCION)){

               if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                   TramosConduccionDialog dialog = new TramosConduccionDialog(true);
                   dialog.setVisible(true);
                   if (dialog.getTramosConduccionPanel().getOkPressed()){

                	   TramosConduccionEIEL abst = dialog.getTramosConduccion(null);
                       GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
                       String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                       InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                   }
               }
               else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                   if (getSelectedElement()!= null && getSelectedElement() instanceof TramosConduccionEIEL){

                	   TramosConduccionEIEL selectedElement = (TramosConduccionEIEL) getSelectedElement();
                       String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                       if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                           JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                   + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                           TramosConduccionDialog dialog = new TramosConduccionDialog(selectedElement, false);
                           dialog.setVisible(true);  

                       }
                       else{

                           bloquearElemento(selectedElement, patron,true);
                           TramosConduccionDialog dialog = new TramosConduccionDialog(selectedElement,true);
                           dialog.setVisible(true);  

                           if (dialog.getTramosConduccionPanel().getOkPressed()){
                        	   TramosConduccionEIEL abst =  dialog.getTramosConduccion((TramosConduccionEIEL) selectedElement);

                               GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
                               String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                               InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                           }
                           bloquearElemento(getSelectedElement(), patron,false);
                       }
                   }
               }
              }   
        	  else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

                  if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){

                      ColectoresDialog dialog = new ColectoresDialog(true);
                      dialog.setVisible(true);
                      if (dialog.getColectoresPanel().getOkPressed()){

                    	  ColectorEIEL abst = dialog.getColector(null);
                          GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
                          String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                          InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                      }
                  }
                  else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){

                      if (getSelectedElement()!= null && getSelectedElement() instanceof ColectorEIEL){

                    	  ColectorEIEL selectedElement = (ColectorEIEL) getSelectedElement();
                          String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                          if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                              JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                      + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                              ColectoresDialog dialog = new ColectoresDialog(selectedElement, false);
                              dialog.setVisible(true);  

                          }
                          else{

                              bloquearElemento(selectedElement, patron,true);
                              ColectoresDialog dialog = new ColectoresDialog(selectedElement,true);
                              dialog.setVisible(true);  

                              if (dialog.getColectoresPanel().getOkPressed()){
                            	  ColectorEIEL abst =  dialog.getColector((ColectorEIEL) selectedElement);

                                  GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
                                  String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                  InitEIEL.clienteLocalGISEIEL.insertarElemento(abst, idLayer, selectedPattern);
                              }
                              bloquearElemento(getSelectedElement(), patron,false);
                          }
                      }
                  }
              } 
        	  else if (selectedPattern.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
        		  if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_ANNADIR)){
  
          			EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog(true);
          			dialog.setVisible(true);
          			if (dialog.getEntidadesAgrupadasPanel().getOkPressed()){

          				EntidadesAgrupadasEIEL agrup = dialog.getEntidadesAgrupadas(null);
          				String idLayer=null;

          				InitEIEL.clienteLocalGISEIEL.insertarElemento(agrup, idLayer, selectedPattern);

          			}
          		}
          		else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_MODIFICAR)){
          			
          			if (getSelectedElement()!= null && getSelectedElement() instanceof EntidadesAgrupadasEIEL){

          				EntidadesAgrupadasEIEL selectedElement = (EntidadesAgrupadasEIEL) getSelectedElement();
                        String bloqueado= InitEIEL.clienteLocalGISEIEL.bloqueado(selectedElement,selectedPattern);
                        if (bloqueado!=null && !bloqueado.equalsIgnoreCase(com.geopista.security.SecurityManager.getPrincipal()!=null?com.geopista.security.SecurityManager.getPrincipal().getName():UserPreferenceStore.getUserPreference(UserPreferenceConstants.DEFAULT_LOGIN_USERNAME,"",false))){

                            JOptionPane.showMessageDialog(this, I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag1") + " " 
                                    + bloqueado + "\n" + I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag2"));

                            EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog(selectedElement, false);
                            dialog.setVisible(true);  

                        }
                        else{

                            bloquearElemento(selectedElement, patron,true);
                            EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog(selectedElement,true);
                            dialog.setVisible(true);  

                            if (dialog.getEntidadesAgrupadasPanel().getOkPressed()){
                            	EntidadesAgrupadasEIEL agrup =  dialog.getEntidadesAgrupadas((EntidadesAgrupadasEIEL) selectedElement);

                                GeopistaLayer geopistaLayer= (GeopistaLayer) GeopistaEditorPanel.getEditor().getLayerManager().getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
                                String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
                                InitEIEL.clienteLocalGISEIEL.insertarElemento(agrup, idLayer, selectedPattern);
                            }
                            bloquearElemento(getSelectedElement(), patron,false);
                        }
                    }
          		}
         }
    }
    
    private void bloquearElemento(final Object elemento, final String tipoElemento, final boolean bloquear){
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
        progressDialog.setTitle(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag3"));
        progressDialog.report(I18N.get("LocalGISEIEL","localgiseiel.mensajes.tag4"));
        progressDialog.addComponentListener(new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                new Thread(new Runnable(){
                    public void run(){
                        try{
                        	InitEIEL.clienteLocalGISEIEL.bloquearElemento(elemento, bloquear, tipoElemento);
                        }
                        catch(Exception e){                            
                            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
                                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
                            return;
                        }
                        finally{
                            progressDialog.setVisible(false);
                        }
                    }
              }).start();
          }
       });
       GUIUtil.centreOnWindow(progressDialog);
       progressDialog.setVisible(true);

    }

    private void updateFeatures(Object[] listafeatures){
        
        if (listafeatures!= null && listafeatures.length>0){

            ArrayList lstLayers = (ArrayList) GeopistaEditorPanel.getEditor().getLayerManager().getLayers();
            for (Iterator iterLayers = lstLayers.iterator(); iterLayers.hasNext();){
                Object obj = iterLayers.next();
                FeatureEvent event = null;
                if(obj instanceof Layer || obj instanceof GeopistaLayer){

                    ArrayList lstFeatures = new ArrayList();
                    for (int i=0; i<listafeatures.length; i++){
                        Object feature = listafeatures[i];
                        if (feature instanceof GeopistaFeature){
                            GeopistaFeature geopistaFeature = (GeopistaFeature)feature;                                     
                            if (geopistaFeature.getLayer().equals(obj)){
                                lstFeatures.add(geopistaFeature);
                            }
                        }
                    }
                    event = new FeatureEvent(lstFeatures,FeatureEventType.GEOMETRY_MODIFIED,(Layer)obj,lstFeatures);                                
                }
                GeopistaEditorPanel.getEditor().featuresChanged(event);
            }
        }
    }
    
    private boolean confirm(String tag1, String tag2){
        int ok= -1;
        ok= JOptionPane.showConfirmDialog(this, tag1, tag2, JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.NO_OPTION){
            return false;
        }
        return true;
    }
    
    private ArrayList obtenerListaDatos(String tipo){

        ArrayList lstElements = new ArrayList();
        try {
            
            String filter = Const.REVISION_EXPIRADA+" =9999999999  ";
            lstElements = InitEIEL.clienteLocalGISEIEL.getLstElementos(filter, tipo,getJPanelBotonera().getJCheckBoxFiltroGeometrias().isSelected());
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstElements;
    }
    
    private Collection getIdFeatures(Object object, String selectedPattern){
        
        Collection lstIdFeatures = null;
        try {
            
            lstIdFeatures = InitEIEL.clienteLocalGISEIEL.getIdsFeatures(object, selectedPattern);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return lstIdFeatures;
    }
    
    private int getModelIndex(int index){
        return ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).modelIndex(index);
    }
    

}
