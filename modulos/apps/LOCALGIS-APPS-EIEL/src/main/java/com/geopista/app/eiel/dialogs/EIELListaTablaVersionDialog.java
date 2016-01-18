/**
 * EIELListaTablaVersionDialog.java
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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import com.geopista.app.AppContext;
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
import com.geopista.app.eiel.models.ColectoresCompletoEIELTableModel;
import com.geopista.app.eiel.models.EntidadesAgrupadasCompletoEIELTableModel;
import com.geopista.app.eiel.models.TramosConduccionCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.AbastecimientoAutonomoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CabildoConsejoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CaptacionesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CasasConsistorialesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CementeriosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CentrosAsistencialesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CentrosCulturalesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CentrosEnsenianzaVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.CentrosSanitariosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.ColectoresVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.DepositosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.Depuradora1VersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.Depuradora2VersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.DiseminadosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.EdificiosSinUsoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.EmisariosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.Encuestados1VersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.Encuestados2VersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.EntidadesAgrupadasVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.EntidadesSingularesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.IncendiosProteccionVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.InfoTerminosMunicipalesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.InstalacionesDeportivasVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.LonjasMercadosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.MataderosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.NucleosAbandonadosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.NucleosPoblacionVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.OtrosServMunicipalesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.PadronMunicipiosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.PadronNucleosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.ParquesJardinesVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.PlaneamientoUrbanoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.PoblamientoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.PuntosVertidoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.RecogidaBasurasVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.SaneamientoAutonomoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.ServiciosAbastecimientosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.ServiciosRecogidaBasuraVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.ServiciosSaneamientoVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.TanatoriosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.TramoConduccionVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.TramosCarreterasVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.TratamientosPotabilizacionVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.models.versionados.VertederosVersionadoCompletoEIELTableModel;
import com.geopista.app.eiel.panels.BotoneraVersionJPanel;
import com.geopista.app.eiel.panels.EditingInfoPanel;
import com.geopista.app.eiel.panels.FiltroVersionJPanel;
import com.geopista.app.eiel.panels.GeopistaEditorPanel;
import com.geopista.app.eiel.panels.ListaDatosEIELJPanel;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.model.GeopistaLayer;
import com.geopista.protocol.administrador.Usuario;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
 


/**
 * @author seilagamo
 *
 */
public class EIELListaTablaVersionDialog extends JDialog {
    
    private static Toolkit tk = Toolkit.getDefaultToolkit();
    private static Dimension d = tk.getScreenSize();
    public static final int DIM_X = 850;
    public static final int DIM_Y = 550;
    private String tableModelCompleto = null;
    private String patron = null;
    private String titulo = null;
    
    private BotoneraVersionJPanel jPanelBotonera = null;
    private FiltroVersionJPanel jPanelFiltro;
    private JTable tablaDatos = null;  
    private TableModel tableListaDatosModel = null;
    
    private ListaDatosEIELJPanel jPanelListaElementos = null;
//    private JPanel panelPrincipal = null;
	private Object elemento;
	
	private String fechaVersionInicio;
	private String fechaVersionFin;
	private String accionVersion;
	private String usuarioVersion;
	private JComboBox jComboTabla;
	private JComboBox jComboUsuario;
	private JPanel jpanel;
	
	
    public EIELListaTablaVersionDialog() throws HeadlessException {
        super(AppContext.getApplicationContext().getMainFrame());
        
        this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.generico");
        //Se añade un nuevo filtro 
        JLabel jLabelTabla = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelTabla"), JLabel.CENTER);
        JLabel jLabelUsuario = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelUsuario"), JLabel.CENTER);

        getJPanelFiltro().getJPanelBotones().add(jLabelUsuario, new GridBagConstraints(0,5,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
        getJPanelFiltro().getJPanelBotones().add(getJComboUsuario(), new GridBagConstraints(1,5,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
        
        getJPanelFiltro().getJPanelBotones().add(jLabelTabla, new GridBagConstraints(0,6,3,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
        getJPanelFiltro().getJPanelBotones().add(getJComboTabla(), new GridBagConstraints(1,6,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
        
        initialize(titulo);

    }  
	
    public EIELListaTablaVersionDialog(Object obj) throws HeadlessException {
        super(AppContext.getApplicationContext().getMainFrame());
        
        cargaDatosIniciales(obj);

        this.elemento = obj;
        
        JLabel jLabelUsuario = new JLabel(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroVersionJPanel.labelUsuario"), JLabel.CENTER);
        
        
        getJPanelFiltro().getJPanelBotones().add(jLabelUsuario, new GridBagConstraints(0,5,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(2,5,0,3),0,0));
        getJPanelFiltro().getJPanelBotones().add(getJComboUsuario(), new GridBagConstraints(1,5,1,1,1, 1,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(2,5,0,3),0,0));
        
        initialize(titulo);
    }  
	
    
    private void cargaDatosIniciales(Object obj) {
		// TODO Auto-generated method stub
    	
		if (obj instanceof AbastecimientoAutonomoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
			 //MARKED
		} else if (obj instanceof CaptacionesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CAPTACIONES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CAPTACIONES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ca");
		} else if (obj instanceof Depuradora1EIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.DEPURADORAS1_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DEPURADORAS1;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.d1");
		} else if (obj instanceof Depuradora2EIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.DEPURADORAS2_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DEPURADORAS2;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.d2");
		} else if (obj instanceof CasasConsistorialesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CASAS_CONSISTORIALES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cc");
		} else if (obj instanceof CentrosCulturalesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_CULTURALES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CENTROS_CULTURALES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cu");
		} else if (obj instanceof CentrosEnsenianzaEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CENTROS_ENSENIANZA;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.en");
		} else if (obj instanceof InstalacionesDeportivasEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.id");
		} else if (obj instanceof NucleosPoblacionEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.NUCLEOS_POBLACION_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.NUCLEOS_POBLACION;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof OtrosServMunicipalesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ot");
		} else if (obj instanceof PadronNucleosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.PADRON_NUCLEOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.PADRON_NUCLEOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pn");
		} else if (obj instanceof PadronMunicipiosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.PADRON_MUNICIPIOS;
			 this.titulo = I18N.get("LocalGISEIEL","PADRON_MUNICIPIOS");
		} else if (obj instanceof ParquesJardinesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.PARQUES_JARDINES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.PARQUES_JARDINES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pj");
		} else if (obj instanceof PlaneamientoUrbanoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof PoblamientoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.POBLAMIENTO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.POBLAMIENTO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof RecogidaBasurasEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.RECOGIDA_BASURAS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.RECOGIDA_BASURAS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.rb");
		} else if (obj instanceof CentrosSanitariosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_SANITARIOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CENTROS_SANITARIOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cs");
		} else if (obj instanceof SaneamientoAutonomoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.sa");
		} else if (obj instanceof ServiciosSaneamientoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof ServiciosRecogidaBasuraEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.srb");
		} else if (obj instanceof EdificiosSinUsoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof TanatoriosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.TANATORIOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.TANATORIOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof VertederosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.VERTEDEROS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DATOS_VERTEDEROS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof TramosCarreterasEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CARRETERAS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.TRAMOS_CARRETERAS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.carrt");
		} else if (obj instanceof DepositosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.DEPOSITOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DEPOSITOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.de");
		} else if (obj instanceof PuntosVertidoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.PUNTOS_VERTIDO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.PUNTOS_VERTIDO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pv");
		} else if (obj instanceof ServiciosAbastecimientosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof CentrosAsistencialesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.as");
		} else if (obj instanceof CabildoConsejoEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CABILDO_CONSEJO_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CABILDO;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ci");
		} else if (obj instanceof CementeriosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.CEMENTERIOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.CEMENTERIOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ce");
		} else if (obj instanceof EntidadesSingularesEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.ENTIDADES_SINGULARES;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ce");
		} else if (obj instanceof NucleoEncuestado7EIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.NUCLEO_ENCT_7;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof IncendiosProteccionEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.INCENDIOS_PROTECCION;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ip");
		} else if (obj instanceof LonjasMercadosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.LONJAS_MERCADOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.LONJAS_MERCADOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.lm");
		} else if (obj instanceof MataderosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.MATADEROS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.MATADEROS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au");
		} else if (obj instanceof TratamientosPotabilizacionEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.abast_tp");
		} else if (obj instanceof DiseminadosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.DISEMINADOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.DISEMINADOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.diseminados");
		} else if (obj instanceof Encuestados1EIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.ENCUESTADOS1_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.ENCUESTADOS1;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.enc1");
		} else if (obj instanceof Encuestados2EIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.ENCUESTADOS2_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.ENCUESTADOS2;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.enc2");
		} else if (obj instanceof NucleosAbandonadosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.aband");
		}		
		//Prueba de concepto Elementos sin informacion alfanumerica.
		//ALFANUMERICOS
		//- EMISARIOS
		else if (obj instanceof EmisariosEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.EMISARIOS_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.EMISARIOS;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.em");
		}
		//Prueba de concepto Elementos sin informacion alfanumerica.
		//ALFANUMERICOS
		//- EMISARIOS
		else if (obj instanceof TramosConduccionEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.TCONDUCCION_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.TCONDUCCION;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.tc");
		}
		//Prueba de concepto Elementos sin informacion alfanumerica.
		//ALFANUMERICOS
		//- EMISARIOS
		else if (obj instanceof ColectorEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.TCOLECTOR_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.TCOLECTOR;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cl");
		}
		else if (obj instanceof EntidadesAgrupadasEIEL) {
	   		 this.tableModelCompleto = ConstantesLocalGISEIEL.AGRUPACIONES6000_MODEL_COMPLETO_VERSIONADO;
			 this.patron = ConstantesLocalGISEIEL.AGRUPACIONES6000;
			 this.titulo = I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.a6");
		}
	}

    public JComboBox getJComboUsuario(){
    	if (jComboUsuario  == null){
    		//Obtener los usuarios que estén asociados a esta entidad
    		Integer idEntidadOriginal=(Integer)AppContext.getApplicationContext().getBlackboard().get("IDENTIDAD_ORIGINAL");
    		ArrayList<Usuario> usuarios = obtenerListaUsuarios(AppContext.getIdEntidad(),idEntidadOriginal);
    		
    		ObjetoComboUser o;

    		ArrayList<ObjetoComboUser> users = new ArrayList<ObjetoComboUser>();
    		users.add(new ObjetoComboUser("",""));
    		
    		for (int i=0;i<usuarios.size();i++){
    			o = new ObjetoComboUser(usuarios.get(i).getName(), usuarios.get(i).getId());
    			users.add(o);
    		}

    		ObjetoComboUser[] objetos = new ObjetoComboUser[users.size()];
    		objetos= users.toArray(objetos);
    		jComboUsuario = new JComboBox(objetos);
    		
    	}
    	return jComboUsuario;
    }
   
    
    public JComboBox getJComboTabla(){
    	if (jComboTabla == null){
    		//Creo los objetos del Combobox
    		ObjetoCombo[] tablas = {new ObjetoCombo("",""), 
    				new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.au"),ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO),
    				//MARKED
    				new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ca"),ConstantesLocalGISEIEL.CAPTACIONES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.d1"),ConstantesLocalGISEIEL.DEPURADORAS1),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.d2"),ConstantesLocalGISEIEL.DEPURADORAS2),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cc"),ConstantesLocalGISEIEL.CASAS_CONSISTORIALES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cu"),ConstantesLocalGISEIEL.CENTROS_CULTURALES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.en"),ConstantesLocalGISEIEL.CENTROS_ENSENIANZA),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.id"),ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS),
//		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.np"),ConstantesLocalGISEIEL.NUCLEOS_POBLACION),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ot"),ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pn"),ConstantesLocalGISEIEL.PADRON_NUCLEOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pm"),ConstantesLocalGISEIEL.PADRON_MUNICIPIOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pj"),ConstantesLocalGISEIEL.PARQUES_JARDINES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pu"),ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pobl"),ConstantesLocalGISEIEL.POBLAMIENTO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.rb"),ConstantesLocalGISEIEL.RECOGIDA_BASURAS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cs"),ConstantesLocalGISEIEL.CENTROS_SANITARIOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.sa"),ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ss"),ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.srb"),ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.su"),ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ta"),ConstantesLocalGISEIEL.TANATORIOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.vt"),ConstantesLocalGISEIEL.DATOS_VERTEDEROS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.tc"),ConstantesLocalGISEIEL.TRAMOS_CARRETERAS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.de"),ConstantesLocalGISEIEL.DEPOSITOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.pv"),ConstantesLocalGISEIEL.PUNTOS_VERTIDO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.serv_abast"),ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.as"),ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ci"),ConstantesLocalGISEIEL.CABILDO),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ce"),ConstantesLocalGISEIEL.CEMENTERIOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ent_sing"),ConstantesLocalGISEIEL.ENTIDADES_SINGULARES),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.inf_ttmm"),ConstantesLocalGISEIEL.NUCLEO_ENCT_7),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.ip"),ConstantesLocalGISEIEL.INCENDIOS_PROTECCION),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.lm"),ConstantesLocalGISEIEL.LONJAS_MERCADOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.mt"),ConstantesLocalGISEIEL.MATADEROS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.abast_tp"),ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.diseminados"),ConstantesLocalGISEIEL.DISEMINADOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.enc1"),ConstantesLocalGISEIEL.ENCUESTADOS1),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.enc2"),ConstantesLocalGISEIEL.ENCUESTADOS2),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.aband"),ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS),
		    		//Prueba de concepto Elementos sin informacion alfanumerica.
		    		//ALFANUMERICOS
		    		//- EMISARIOS
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.em"),ConstantesLocalGISEIEL.EMISARIOS),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.cn"),ConstantesLocalGISEIEL.TCONDUCCION),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.co"),ConstantesLocalGISEIEL.TCOLECTOR),
		    		new ObjetoCombo(I18N.get("LocalGISEIEL","localgiseiel.dialog.titulo.a6"),ConstantesLocalGISEIEL.AGRUPACIONES6000),
    		        new ObjetoCombo("Infraestructura Viaria",ConstantesLocalGISEIEL.INFRAESTRUCTURAS_VIARIAS)

    			};
    		Arrays.sort(tablas);
    		
    		jComboTabla = new JComboBox(tablas);
    		jComboTabla.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					patron = ((ObjetoCombo)jComboTabla.getSelectedItem()).getTipo();
					
					//Se fija el modelo correcto para cada caso
					if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO;
				   	//MARKED	 
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CAPTACIONES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CAPTACIONES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPURADORAS1)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.DEPURADORAS1_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPURADORAS2)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.DEPURADORAS2_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_CULTURALES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_CULTURALES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.NUCLEOS_POBLACION_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.PADRON_NUCLEOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.PADRON_NUCLEOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.PARQUES_JARDINES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.PARQUES_JARDINES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.POBLAMIENTO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.POBLAMIENTO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.RECOGIDA_BASURAS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_SANITARIOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.TANATORIOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.TANATORIOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.VERTEDEROS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CARRETERAS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DEPOSITOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.DEPOSITOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.PUNTOS_VERTIDO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CABILDO)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CABILDO_CONSEJO_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.CEMENTERIOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.CEMENTERIOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.LONJAS_MERCADOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.LONJAS_MERCADOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.MATADEROS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.MATADEROS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.DISEMINADOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.DISEMINADOS_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.ENCUESTADOS1)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.ENCUESTADOS1_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.ENCUESTADOS2)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.ENCUESTADOS2_MODEL_COMPLETO_VERSIONADO;
					} else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_MODEL_COMPLETO_VERSIONADO;
					}
					//Prueba de concepto Elementos sin informacion alfanumerica.
					//ALFANUMERICOS
					//- EMISARIOS
				     else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.EMISARIOS)) {
					   		 tableModelCompleto = ConstantesLocalGISEIEL.EMISARIOS_MODEL_COMPLETO_VERSIONADO;					 	
					}
				     else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.TCONDUCCION)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.TCONDUCCION_MODEL_COMPLETO_VERSIONADO;					 	
				     }
				     else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.TCOLECTOR)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.TCOLECTOR_MODEL_COMPLETO_VERSIONADO;					 	
				     }
				     else if (patron.equalsIgnoreCase(ConstantesLocalGISEIEL.AGRUPACIONES6000)) {
				   		 tableModelCompleto = ConstantesLocalGISEIEL.AGRUPACIONES6000_MODEL_COMPLETO_VERSIONADO;					 	
				     }

				}
				
    		});
    	}
    	return jComboTabla;
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
        
        JPanel panel = getJPanel();
        this.getContentPane().add(panel);
        
        listarDatosTabla();

    }
    
    public JPanel getJPanel(){
    	 if (jpanel ==null){
    		 jpanel = new JPanel(new GridBagLayout());
         
    		 jpanel.add(getJPanelFiltro(),				new GridBagConstraints(0, 0, 1, 1, 0, 0,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    		 jpanel.add(getJPanelBotonera(), 			new GridBagConstraints(0, 2, 1, 1, 0, 0,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 0), 0, 0));
    		 jpanel.add(getJPanelListaElementos(),      new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 15, 0), 0, 0));
    	 }
         return jpanel ;
    }
    
    
    private BotoneraVersionJPanel getJPanelBotonera(){
        
        if (jPanelBotonera == null){
            
            jPanelBotonera = new BotoneraVersionJPanel();          
            jPanelBotonera.setEnabled(false);
            jPanelBotonera.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    botoneraJPanel_actionPerformed();
                }
            });

        }
        return jPanelBotonera;
    }
    
    private FiltroVersionJPanel getJPanelFiltro(){
        
        if (jPanelFiltro == null){
            
        	jPanelFiltro = new FiltroVersionJPanel();          
        	jPanelFiltro.setEnabled(false);
        	jPanelFiltro.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    filtroJPanel_actionPerformed();
                }
            });

        }
        return jPanelFiltro;
    }
     
    public void filtroJPanel_actionPerformed(){
    	
        try{
        	resetFiltro();
        	//Aplicar filtros
        	
        	//Si se ha introducido fecha, se obtiene su valor
        	Date fechaInicio = this.getJPanelFiltro().getJTextFieldFechaInicio().getDate();
        	String horaInicio = this.getJPanelFiltro().getJTextFieldHoraInicio().getText();
        	Date fechaFin = this.getJPanelFiltro().getJTextFieldFechaFin().getDate();
        	String horaFin = this.getJPanelFiltro().getJTextFieldHoraFin().getText();
        	if (fechaInicio !=null){
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        		if (horaInicio!=null)
        			fechaVersionInicio =formatter.format(fechaInicio) + " "+horaInicio;
        		else
        			fechaVersionInicio =formatter.format(fechaInicio);
        	}
        	if (fechaFin !=null){
        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ");
        		if (horaFin!=null)
        			fechaVersionFin=formatter.format(fechaFin) + " "+horaFin;
        		else
        			fechaVersionFin =formatter.format(fechaFin);
        	}
        	//Se convierte a minusculas, que es ocmo se introduce en la BBDD
        	accionVersion = ((String) this.getJPanelFiltro().getJComboAccion().getSelectedItem());
        	usuarioVersion = ((ObjetoComboUser)this.getJComboUsuario().getSelectedItem()).getId();
        	
      		if (patron != null && !patron.equals("")){	
            listarDatosTabla();
      		}
      		else
      			JOptionPane.showMessageDialog(this, "Seleccione un tipo de Elemento a mostrar","Versionao", JOptionPane.INFORMATION_MESSAGE);
            
           
        }catch (Exception e){
            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
        }
        getJPanelFiltro().setBotonPressed(null);
        
    }
    
    private void resetFiltro(){
    	fechaVersionInicio = null;
    	fechaVersionFin = null;
    	accionVersion = null;
    	usuarioVersion = null;
    }
   
    public void botoneraJPanel_actionPerformed(){
        try{
            abrirDialogo(patron, getJPanelBotonera().getBotonPressed());
            listarDatosTabla();
           
        }catch (Exception e){
            ErrorDialog.show(AppContext.getApplicationContext().getMainFrame(), 
                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.title"), 
                    I18N.get("LocalGISEIEL","localgiseiel.sqlerror.warning"), StringUtil.stackTrace(e));
        }
        getJPanelBotonera().setBotonPressed(null);

    }
    
  
	private JPanel getJPanelListaElementos(){
        
        if (jPanelListaElementos == null ){
            
        	if (tableModelCompleto!=null)
        		jPanelListaElementos  = new ListaDatosEIELJPanel(tableModelCompleto);
        	else
        		jPanelListaElementos  = new ListaDatosEIELJPanel();
        	
            jPanelListaElementos.getJTableListaDatos().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            jPanelListaElementos.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(ActionEvent e){
                    listaelementosJPanel_actionPerformed();
                }
            });
            
//            getJPanel().add(getJPanelListaElementos(),      new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        }
        return jPanelListaElementos;
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
    	
//    	Object selectedItem = this.getSelectedElement();
    	getJPanelBotonera().getJButtonVerVersion().setEnabled(true);   
       
        if (patron != null && !patron.equals("")){

        	            
            if (patron.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO);
        
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO);
                AbastecimientoAutonomoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (AbastecimientoAutonomoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINENucleo(),
							obj.getViviendas(), 
							obj.getPoblacionResidente(),
							obj.getPoblacionEstacional(),
							obj.getViviendasDeficitarias(),
							obj.getPoblacionResidenteDef(),
							obj.getPoblacionEstacionalDef(),
							obj.getFuentesControladas(),
							obj.getFuentesNoControladas(),
							obj.getSuficienciaCaudal(),
							obj.getObservaciones(), 
							obj.getFechaRevision(),
							obj.getEstadoRevision(), 
							obj.getBloqueado() };
					rows.add(rowData);

				}        

				
                ((AbastecimientoAutonomoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);                
                
            }
            //MARKED
            else if (patron.equals(ConstantesLocalGISEIEL.CAPTACIONES)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CAPTACIONES_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CAPTACIONES);
                
            	
				CaptacionesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CaptacionesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodOrden(), 
							obj.getNombre(), 
							obj.getTipo(),
							obj.getTitular(), 
							obj.getGestor(),
							obj.getSistema(), 
							obj.getEstado(),
							obj.getUso(), 
							obj.getProteccion(),
							obj.getContador(), 
							obj.getObservaciones(),
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getFechaInst(), 
							obj.getCuenca(),
							obj.getN_expediente(), 
							obj.getN_inventario(),
							obj.getCota(), 
							obj.getProfundidad(),
							obj.getMax_consumo(), 
							obj.getBloqueado() };
					rows.add(rowData);

				}
                
                ((CaptacionesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
                

                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

                setNameTableModel(ConstantesLocalGISEIEL.DEPURADORAS1_MODEL_COMPLETO_VERSIONADO);
                refrescarPanel();
                
				ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DEPURADORAS1);
				Depuradora1EIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (Depuradora1EIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodOrden(), 
							obj.getTratPrimario1(),
							obj.getTratPrimario2(),
							obj.getTratPrimario3(),
							obj.getTratSecundario1(),
							obj.getTratSecundario2(),
							obj.getTratSecundario3(), 
							obj.getTratAvanzado1(),
							obj.getTratAvanzado2(), 
							obj.getTratAvanzado3(),
							obj.getProcComplementario1(),
							obj.getProcComplementario2(),
							obj.getProcComplementario3(),
							obj.getTratLodos1(), 
							obj.getTratLodos2(), 
							obj.getTratLodos3(),
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getBloqueado()};
					rows.add(rowData);

				}		
				
				
                ((Depuradora1VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){
                
                setNameTableModel(ConstantesLocalGISEIEL.DEPURADORAS2_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DEPURADORAS2);
				Depuradora2EIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (Depuradora2EIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodOrden(), 
							obj.getTitular(),
							obj.getGestor(), 
							obj.getCapacidad(),
							obj.getProblemas1(), 
							obj.getProblemas2(),
							obj.getProblemas3(), 
							obj.getGestionLodos(),
							obj.getLodosVertedero(),
							obj.getLodosIncineracion(),
							obj.getLodosAgrConCompostaje(),
							obj.getLodosAgrSinCompostaje(),
							obj.getLodosOtroFinal(),
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(),
							obj.getEstadoRevision(), 
							obj.getBloqueado() };
					rows.add(rowData);

				}
                ((Depuradora2VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES);
				CasasConsistorialesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CasasConsistorialesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(),
							obj.getCodOrden(),
							obj.getNombre(), 
							obj.getTipo(), 
							obj.getTitular(),
							obj.getTenencia(), 
							obj.getSupCubierta(),
							obj.getSupAire(), 
							obj.getSupSolar(), 
							obj.getEstado(),
							obj.getFechaInstalacion(), 
							obj.getObservaciones(),
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getBloqueado(), 
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec(),
							obj.getListaUsos()};
					rows.add(rowData);

				}
				
                ((CasasConsistorialesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_CULTURALES_MODEL_COMPLETO_VERSIONADO);
                refrescarPanel();             
                getJPanel().add(getJPanelListaElementos(),      new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                getJPanel().revalidate();
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_CULTURALES);
                CentrosCulturalesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CentrosCulturalesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(),
							obj.getCodOrden(),
							obj.getNombre(),
							obj.getTipo(), 
							obj.getTitular(),
							obj.getGestor(), 
							obj.getSupCubierta(),
							obj.getSupAire(), 
							obj.getSupSolar(),
							obj.getEstado(), 
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec(),
							obj.getListaUsos()
						};
					rows.add(rowData);

				}   
				
                ((CentrosCulturalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_MODEL_COMPLETO_VERSIONADO);
                refrescarPanel();
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA);
                CentrosEnsenianzaEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CentrosEnsenianzaEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(), 
							obj.getCodOrden(),
							obj.getNombre(), 
							obj.getAmbito(),
							obj.getTitular(), 
							obj.getSupCubierta(),
							obj.getSupAire(), 
							obj.getSupSolar(),
							obj.getEstado(), 
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec(),
							obj.getListaNiveles()
						};
					rows.add(rowData);					
				}  
                ((CentrosEnsenianzaVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS);
                InstalacionesDeportivasEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (InstalacionesDeportivasEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(), 
							obj.getOrdenIdDeportes(),
							obj.getNombre(), 
							obj.getTipo(),
							obj.getTitular(),
							obj.getGestor(), 
							obj.getSupCubierta(),
							obj.getSupAire(), 
							obj.getSupSolar(),
							obj.getEstado(), 
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec(),
							obj.getInst_P(),
							obj.getListaTipos()
						};
					rows.add(rowData);
				}
				
                ((InstalacionesDeportivasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){
                
                setNameTableModel(ConstantesLocalGISEIEL.NUCLEOS_POBLACION_MODEL_COMPLETO_VERSIONADO);
                refrescarPanel();             
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.NUCLEOS_POBLACION);
                NucleosPoblacionEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (NucleosPoblacionEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(), 
							obj.getFechaRevision(), 
							obj.getEstadoActualizacion()
						};
					rows.add(rowData);
				}
				
                ((NucleosPoblacionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){
                
                setNameTableModel(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES);
                OtrosServMunicipalesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (OtrosServMunicipalesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getSwInfGeneral(), 
							obj.getSwInfTuristica(), 
							obj.getSwGbElectronico(), 
							obj.getOrdSoterramiento(),
							obj.getEnEolica(), 
							obj.getKwEolica(),
							obj.getEnSolar(), 
							obj.getKwSolar(), 
							obj.getPlMareomotriz(), 
							obj.getKwMareomotriz(),
							obj.getOtEnergias(), 
							obj.getKwOtEnergias(), 
							obj.getFechaRevision(), 
							obj.getObservaciones(),
							obj.getEstadoRevision(), 
							obj.getCoberturaTlf(),
							obj.getTeleCable(), 
							obj.getBloqueado() 
						};
					rows.add(rowData);
				}                		
				
                ((OtrosServMunicipalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.PADRON_NUCLEOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PADRON_NUCLEOS);
                PadronNucleosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (PadronNucleosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(), 
							obj.getHombres_a1(), 
							obj.getMujeres_a1(),
							obj.getTotPobl_a1(), 
							obj.getHombres_a2(),
							obj.getMujeres_a2(), 
							obj.getTotPobl_a2(), 
							obj.getFecha_a1(), 
							obj.getFecha_a2(),
							obj.getFechaRevision(), 
							obj.getObservaciones(), 
							obj.getEstadoRevision(), 
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                      

				
                ((PadronNucleosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS);
                PadronMunicipiosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (PadronMunicipiosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getHombres_a1(), 
							obj.getMujeres_a1(),
							obj.getTotPobl_a1(), 
							obj.getHombres_a2(),
							obj.getMujeres_a2(), 
							obj.getTotPobl_a2(), 
							obj.getFecha_a1(), 
							obj.getFecha_a2(),
							obj.getFechaActualizacion(), 
							obj.getEstadoRevision(), 
							obj.getObservaciones(), 
							obj.getBloqueado()
						};
					rows.add(rowData);
				}  
				
                ((PadronMunicipiosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){
                
                setNameTableModel(ConstantesLocalGISEIEL.PARQUES_JARDINES_MODEL_COMPLETO_VERSIONADO);
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PARQUES_JARDINES);
                ParquesJardinesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (ParquesJardinesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(), 
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodOrden(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(),
							obj.getNombre(), 
							obj.getTipo(), 
							obj.getTitularidad(), 
							obj.getGestion(),
							obj.getSupCubierta(), 
							obj.getSupLibre(), 
							obj.getSupSolar(), 
							obj.getAgua(), 
							obj.getSaneamiento(),
							obj.getElectricidad(),
							obj.getComedor(),
							obj.getJuegosInf(), 
							obj.getOtros(), 
							obj.getEstado(),
							obj.getFechaRevision(), 
							obj.getEstadoRevision(), 
							obj.getObservaciones(), 	
							obj.getBloqueado(), 
							obj.getAccesoSilla(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}                  
                
                ((ParquesJardinesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO);
                PlaneamientoUrbanoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (PlaneamientoUrbanoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getTipo(),
							obj.getEstado(), 
							obj.getDenominacion(),
							obj.getSupMunicipal(), 
							obj.getFechaPublicacion(), 
							obj.getSupUrbano(), 
							obj.getSupUrbanizable(),
							obj.getSupNoUrbanizable(), 
							obj.getSupNoUrbanizableEsp(), 
							obj.getFechaRevision(), 
							obj.getObservaciones(), 
							obj.getEstadoRevision(),
							obj.getOrden(), 
							obj.getBloqueado()
						};
					rows.add(rowData);
				}             

				
                ((PlaneamientoUrbanoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.POBLAMIENTO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();              
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.POBLAMIENTO);
                PoblamientoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (PoblamientoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getFechaActualizacion(),
							obj.getObservaciones(), 
							obj.getEstadoRevision(),  
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                  
				
                ((PoblamientoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.RECOGIDA_BASURAS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();         
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.RECOGIDA_BASURAS);
                RecogidaBasurasEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (RecogidaBasurasEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getTipo(),
							obj.getGestion(),
							obj.getPeriodicidad(),
							obj.getCalidad(),
							obj.getTonProducidas(),
							obj.getNumContenedores(), 
							obj.getFecharevision(),		
							obj.getObservaciones(), 
							obj.getEstadoRevision(),  
							obj.getBloqueado()
						};
					rows.add(rowData);
				}   			
				
                ((RecogidaBasurasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_SANITARIOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();         
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_SANITARIOS);
                CentrosSanitariosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CentrosSanitariosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getOrden(),
							obj.getNombre(),
							obj.getTipo(),
							obj.getTitularidad(),
							obj.getGestion(),
							obj.getSupCubierta(), 
							obj.getSupLibre(),
							obj.getSupSolar(), 
							obj.getUci(),		
							obj.getNumCamas(), 
							obj.getEstado(),  
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(),
							obj.getEstadoRevision(), 
							obj.getBloqueado(),		
							obj.getAcceso_s_ruedas(), 
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}   			          
						
				
                ((CentrosSanitariosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO);
                SaneamientoAutonomoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (SaneamientoAutonomoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINENucleo(), 
							obj.getTipo(),
							obj.getEstado(),
							obj.getAdecuacion(),
							obj.getViviendas(), 
							obj.getPoblResidente(),
							obj.getPoblEstacional(), 
							obj.getVivDeficitarias(),		
							obj.getPoblResDeficitaria(), 
							obj.getPoblEstDeficitaria(),  
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}        
                ((SaneamientoAutonomoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.SERVICIOS_SANEAMIENTO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO);
                ServiciosSaneamientoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (ServiciosSaneamientoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getPozos(),
							obj.getSumideros(),
							obj.getAlivAcumulacion(),
							obj.getAlivSinAcumulacion(), 
							obj.getCalidad(),
							obj.getVivNoConectadas(), 
							obj.getVivConectadas(),		
							obj.getLongDeficitaria(), 
							obj.getVivDeficitarias(),  
							obj.getPoblResDeficitaria(),
							obj.getPoblEstDeficitaria(), 
							obj.getCaudalTotal(),							
							obj.getcCaudalTratado(),
							obj.getCaudalUrbano(), 
							obj.getCaudalRustico(),
							obj.getCaudalIndustrial(), 
							obj.getObservaciones(),		
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}
								
                ((ServiciosSaneamientoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){
                
                setNameTableModel(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA);
                ServiciosRecogidaBasuraEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (ServiciosRecogidaBasuraEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getVivSinServicio(),
							obj.getPoblResSinServicio(),
							obj.getPoblEstSinServicio(),
							obj.getServLimpCalles(), 
							obj.getPlantilla(),
							obj.getObservaciones(), 
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}
			
                ((ServiciosRecogidaBasuraVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO);
                EdificiosSinUsoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (EdificiosSinUsoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getCodOrden(),
							obj.getNombre(),
							obj.getTitularidad(),
							obj.getSupCubierta(), 
							obj.getSupLibre(),
							obj.getSupSolar(), 
							obj.getEstado(),	
							obj.getUsoAnterior(), 
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(), 
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}  
				
                ((EdificiosSinUsoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TANATORIOS)){
//                
                setNameTableModel(ConstantesLocalGISEIEL.TANATORIOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TANATORIOS);
                TanatoriosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (TanatoriosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getCodOrden(),
							obj.getNombre(),
							obj.getTitularidad(),
							obj.getGestion(), 
							obj.getSupCubierta(), 
							obj.getSupLibre(),
							obj.getSupSolar(),
							obj.getSalas(), 
							obj.getEstado(),	
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(), 
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}                  
				
				
                ((TanatoriosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.VERTEDEROS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DATOS_VERTEDEROS);
                VertederosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (VertederosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodOrden(),
							obj.getTipo(),
							obj.getTitularidad(),
							obj.getGestion(), 
							obj.getOlores(), 
							obj.getHumos(),
							obj.getContAnimal(),
							obj.getRsgoInundacion(),
							obj.getFiltraciones(),
							obj.getImptVisual(),
							obj.getFrecAverias(),
							obj.getSaturacion(),
							obj.getInestabilidad(),							
							obj.getOtros(),
							obj.getCapTotal(),
							obj.getCapOcupada(),
							obj.getCapTransform(),
							obj.getEstado(),	
							obj.getVidaUtil(),
							obj.getCategoria(),
							obj.getActividad(),
							obj.getFechaApertura(),
							obj.getObservaciones(),	
							obj.getPosbAmpliacion(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(), 
							obj.getObra_ejecutada()
						};
					rows.add(rowData);
				}             

                ((VertederosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){
//                
                setNameTableModel(ConstantesLocalGISEIEL.CARRETERAS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();  
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS);
                TramosCarreterasEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (TramosCarreterasEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodCarretera(), 
							obj.getClaseVia(),
							obj.getDenominacion(),
							obj.getTitularidad(),
							obj.getFechaActualizacion(), 
							obj.getObservaciones(), 
							obj.getBloqueado()
						};
					rows.add(rowData);
				}    
				
                ((TramosCarreterasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.DEPOSITOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DEPOSITOS);
                DepositosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (DepositosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getOrdenDeposito(),
							obj.getUbicacion(),
							obj.getTitularidad(),
							obj.getGestor(), 
							obj.getCapacidad(),
							obj.getEstado(),
							obj.getProteccion(),
							obj.getFechaLimpieza(), 
							obj.getContador(),
							obj.getFechaInstalacion(),
							obj.getObservaciones(),
							obj.getFechaRevision(), 
							obj.getEstadoRevision(),							
							obj.getBloqueado()
						};
					rows.add(rowData);
				}              
                
                ((DepositosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){
               
                setNameTableModel(ConstantesLocalGISEIEL.PUNTOS_VERTIDO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.PUNTOS_VERTIDO);
                PuntosVertidoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (PuntosVertidoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getOrden(),
							obj.getTipo(),
							obj.getZona(),
							obj.getDistanciaNucleo(), 
							obj.getFechaInicio(),
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                          

				
                ((PuntosVertidoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){

                setNameTableModel(ConstantesLocalGISEIEL.SERVICIOS_ABASTECIMIENTOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS);
                ServiciosAbastecimientosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (ServiciosAbastecimientosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(),
							obj.getViviendasConectadas(),
							obj.getViviendasNoConectadas(), 
							obj.getConsumoInvierno(),
							obj.getConsumoVerano(),
							obj.getViviendasExcesoPresion(),
							obj.getViviendasDeficitPresion(),
							obj.getPerdidasAgua(),
							obj.getCalidadServicio(),
							obj.getLogitudDeficitaria(), 
							obj.getViviendasDeficitarias(),
							obj.getPoblacionResidenteDeficitaria(),
							obj.getPoblacionEstacionalDeficitaria(),							
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),														
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                 
				
                ((ServiciosAbastecimientosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){

                setNameTableModel(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES);
                CentrosAsistencialesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CentrosAsistencialesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(),
							obj.getOrdenAsistencial(),
							obj.getNombre(), 
							obj.getTipo(),
							obj.getTitularidad(),
							obj.getGestion(),
							obj.getPlazas(),
							obj.getSuperficieCubierta(),
							obj.getSuperficieAireLibre(),
							obj.getSuperficieSolar(), 
							obj.getEstado(),
							obj.getFechaInstalacion(),
							obj.getObservaciones(),							
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(),														
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}                                
				
                ((CentrosAsistencialesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CABILDO)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CABILDO_CONSEJO_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CABILDO);
                CabildoConsejoEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CabildoConsejoEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodIsla(), 
							obj.getDenominacion(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                      
				
                ((CabildoConsejoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.CEMENTERIOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.CEMENTERIOS);
                CementeriosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (CementeriosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(), 
							obj.getCodINEPoblamiento(),
							obj.getOrden(), 
							obj.getNombre(),
							obj.getTitular(), 
							obj.getDistancia(),
							obj.getAcceso(), 
							obj.getCapilla(),
							obj.getDepositoCadaveres(), 
							obj.getAmpliacion(),
							obj.getSaturacion(), 
							obj.getSuperficie(),
							obj.getCrematorio(), 
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFechaRevision(),		
							obj.getEstadoRevision(), 
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(), 
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}             
                ((CementeriosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
                
                setNameTableModel(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES);
                EntidadesSingularesEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (EntidadesSingularesEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getDenominacion(),
							obj.getFechaRevision(), 
							obj.getObservaciones(),
							obj.getEstadoRevision(), 
							obj.getBloqueado()
						};
					rows.add(rowData);
				} 

                ((EntidadesSingularesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
                
                setNameTableModel(ConstantesLocalGISEIEL.INFO_TERMINOS_MUNICIPALES_MODEL_COMPLETO_VERSIONADO);
                         
                refrescarPanel();            
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.NUCLEO_ENCT_7);
                NucleoEncuestado7EIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (NucleoEncuestado7EIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getTvAntena(),
							obj.getTvCable(), 
							obj.getCalidadGSM(),
							obj.getCalidadUMTS(),
							obj.getCalidadGPRS(),
							obj.getCorreos(),
							obj.getRdsi(),
							obj.getAdsl(),
							obj.getWifi(),
							obj.getInternetTV(),
							obj.getInternetRed(),
							obj.getInternetSatelite(),
							obj.getInternetPublico(),
							obj.getCalidadElectricidad(),
							obj.getCalidadGas(),
							obj.getViviendasDeficitariasAlumbrado(),
							obj.getLongitudDeficitariaAlumbrado(),
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};

					rows.add(rowData);
				}                
                ((InfoTerminosMunicipalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
                
                setNameTableModel(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION);
                IncendiosProteccionEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (IncendiosProteccionEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getOrden(),
							obj.getNombre(), 
							obj.getTipo(),
							obj.getTitular(),
							obj.getGestor(),
							obj.getAmbito(),
							obj.getSuperficieCubierta(),
							obj.getSuperficieAireLibre(),
							obj.getSuperficieSolar(),
							obj.getPlantillaProfesionales(),
							obj.getPlantillaVoluntarios(),
							obj.getEstado(),
							obj.getVechiculosIncendios(),
							obj.getVechiculosRescate(),
							obj.getAmbulancias(),
							obj.getMediosAereos(),
							obj.getOtrosVehiculos(),
							obj.getQuitanieves(),
							obj.getSistemasDeteccionIncencios(),
							obj.getOtros(),
							obj.getFechaInstalacion(),
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}                   
						
				
                ((IncendiosProteccionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.LONJAS_MERCADOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.LONJAS_MERCADOS);
                LonjasMercadosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (LonjasMercadosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getOrden(),
							obj.getNombre(), 
							obj.getTipo(),
							obj.getTitular(),
							obj.getGestion(),
							obj.getSuperficieCubierta(),
							obj.getSuperficieAireLibre(),
							obj.getSuperficieSolar(),
							obj.getEstado(),
							obj.getFechaInstalacion(),
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}                      
                ((LonjasMercadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.MATADEROS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.MATADEROS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.MATADEROS);
                MataderosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (MataderosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(), 
							obj.getOrden(),
							obj.getNombre(), 
							obj.getClase(),
							obj.getTitular(),
							obj.getGestion(),
							obj.getSuperficieCubierta(),
							obj.getSuperficieAireLibre(),
							obj.getSuperficieSolar(),
							obj.getEstado(),
							obj.getCapacidadMax(),
							obj.getCapacidadUtilizada(),
							obj.getTunel(),
							obj.getBovino(),
							obj.getOvino(),
							obj.getPorcino(),
							obj.getOtros(),
							obj.getFechaInstalacion(),
							obj.getObservaciones(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado(),
							obj.getAcceso_s_ruedas(),
							obj.getObra_ejec()
						};
					rows.add(rowData);
				}                          

				
                ((MataderosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
                setNameTableModel(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION);
                TratamientosPotabilizacionEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (TratamientosPotabilizacionEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getClave(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(), 
							obj.getOrdenPotabilizadora(),
							obj.getTipo(), 
							obj.getUbicacion(),
							obj.getSoloDesinfeccion(), 
							obj.getCategoriaA2(),
							obj.getCategoriaA3(),
							obj.getCategoriaA3(),
							obj.getDesaladora(),
							obj.getOtros(),
							obj.getMetodoDesinfeccion1(),
							obj.getMetodoDesinfeccion2(),
							obj.getMetodoDesinfeccion3(),
							obj.getPerioricidad(),
							obj.getOrganismoControl(),
							obj.getObserv(),
							obj.getEstado(),
							obj.getFechaInstalacion(),
							obj.getFechaRevision(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                     
				
				
                ((TratamientosPotabilizacionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.DISEMINADOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.DISEMINADOS);
                DiseminadosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (DiseminadosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(),
							obj.getPadron(),
							obj.getPoblacionEstacional(),
							obj.getViviendasTotales(),
							obj.getPlazasHoteleras(),
							obj.getPlazasCasasRurales(),
							obj.getLongitudAbastecimiento(),
							obj.getViviendasConAbastecimiento(),
							obj.getViviendasSinAbastecimiento(),
							obj.getConsumoInvierno(),
							obj.getConsumoVerano(),
							obj.getViviendasExcesoPresion(),
							obj.getViviendasDefectoPresion(),
							obj.getLongDeficitariaAbast(),
							obj.getViviendasDeficitAbast(),
							obj.getPoblacionResidenteDefAbast(),
							obj.getPoblacionEstacionalDefAbast(),
							obj.getPoblacionResidenteAbastAuto(),
							obj.getPoblacionEstacionalAbastAuto(),
							obj.getViviendasDefAbastAuto(),
							obj.getPoblacionResidenteDefAbastAuto(),
							obj.getPoblacionEstacionalDefAbastAuto(),
							obj.getFuentesNoControladas(),
							obj.getLongitudSaneamiento(),
							obj.getViviendasConSaneamiento(),
							obj.getViviendasSinSaneamiento(),
							obj.getLongDeficitariaSaneam(),
							obj.getViviendasDefSaneam(),
							obj.getPoblacionResidenteDefSaneam(),
							obj.getPoblacionEstacionalDefSaneam(),
							obj.getCaudalDesaguado(),
							obj.getCaudalTratado(),
							obj.getViviendasSaneamientoAuto(),
							obj.getPoblacionEstacionalSaneamAuto(),
							obj.getViviendasDeficitSaneamAuto(),
							obj.getPoblacionResidenteDefSaneamAuto(),
							obj.getPoblacionEstacionalDefSaneamAuto(),
							obj.getTmBasura(),
							obj.getContenedores(),	
							obj.getViviendasSinBasura(),	
							obj.getPoblacionResidenteSinBasura(),	
							obj.getPoblacionEstacionalSinBasura(),
							obj.getPlantillaLimpieza(),	
							obj.getPuntosLuz(),	
							obj.getViviendasSinAlumbrado(),	
							obj.getLongDeficitariaAlumbrado(),
							obj.getVivendasAbastecimientoAuto(),	
							obj.getFuentesControladas(),	
							obj.getPoblacionResidenteSaneamAuto(),
							obj.getFecha(),
							obj.getEstado(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}        

                ((DiseminadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
          
                setNameTableModel(ConstantesLocalGISEIEL.ENCUESTADOS1_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ENCUESTADOS1);
                Encuestados1EIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (Encuestados1EIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(),
							obj.getPadron(),
							obj.getPoblacionEstacional(),
							obj.getAltitud(),
							obj.getViviendasTotales(),
							obj.getPlazasHoteleras(),
							obj.getPlazasCasasRurales(),
							obj.getAccesibilidad(),
							obj.getFechaRevision(),
							obj.getObservaciones(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}        
				
                ((Encuestados1VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
                
                setNameTableModel(ConstantesLocalGISEIEL.ENCUESTADOS2_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.ENCUESTADOS2);
                Encuestados2EIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (Encuestados2EIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(),
							obj.getDisponibilidadCaudal(),
							obj.getRestriccionesAgua(),
							obj.getContadores(),
							obj.getTasa(),
							obj.getAnnoInstalacion(),
							obj.getHidrantes(),
							obj.getEstadoHidrantes(),
							obj.getValvulas(),
							obj.getEstadoValvulas(),
							obj.getBocasRiego(),
							obj.getEstadoBocasRiego(),
							obj.getCisterna(),
							obj.getFechaRevision(),
							obj.getObservaciones(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                     			
				
                ((Encuestados2VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }
            else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS);
                NucleosAbandonadosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (NucleosAbandonadosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(),
							obj.getCodINEProvincia(),
							obj.getCodINEMunicipio(),
							obj.getCodINEEntidad(),
							obj.getCodINEPoblamiento(),
							obj.getAnnoAbandono(),
							obj.getCausaAbandono(),
							obj.getTitularidad(),
							obj.getRehabilitacion(),
							obj.getAcceso(),
							obj.getServicioAgua(),
							obj.getServicioElectricidad(),
							obj.getFechaRevision(),
							obj.getObservaciones(),
							obj.getEstadoRevision(),
							obj.getBloqueado()
						};
					rows.add(rowData);
				}                     	
                ((NucleosAbandonadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
            }                   
    		//Prueba de concepto Elementos sin informacion alfanumerica.
    		//ALFANUMERICOS
    		//- EMISARIOS
            else if (patron.equals(ConstantesLocalGISEIEL.EMISARIOS)){
                
                setNameTableModel(ConstantesLocalGISEIEL.EMISARIOS_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.EMISARIOS);
                
            	
				EmisariosEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (EmisariosEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodOrden(), 
							obj.getTitularidad(), 
							obj.getGestion(),
							obj.getEstado(), 
							obj.getMaterial(),
							obj.getSistema(), 
							obj.getTipo_red(),
							obj.getFecha_inst(), 
							obj.getObservaciones(),
							obj.getFechaRevision(), 
							obj.getEstado_Revision(),

							obj.getBloqueado() };
					rows.add(rowData);

				}
                
                ((EmisariosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
                

                
            }
    		//Prueba de concepto Elementos sin informacion alfanumerica.
    		//ALFANUMERICOS
    		//- EMISARIOS
            else if (patron.equals(ConstantesLocalGISEIEL.TCONDUCCION)){
                
                setNameTableModel(ConstantesLocalGISEIEL.TCONDUCCION_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TCONDUCCION);
                
            	
				TramosConduccionEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (TramosConduccionEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getTramo_cn(), 
							obj.getTitular(), 
							obj.getGestor(),
							obj.getEstado(), 
							obj.getMaterial(),
							obj.getSist_trans(), 
							obj.getFechaInstalacion(),
							obj.getObservaciones(), 
							obj.getFecha_revision(),
							obj.getEstado_revision(), 
							obj.getObservaciones(),
							obj.getBloqueado() };
					rows.add(rowData);

				}
                
                ((TramoConduccionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
        
            }
    		//Prueba de concepto Elementos sin informacion alfanumerica.
    		//ALFANUMERICOS
    		//- EMISARIOS
            else if (patron.equals(ConstantesLocalGISEIEL.TCOLECTOR)){
                
                setNameTableModel(ConstantesLocalGISEIEL.TCOLECTOR_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.TCOLECTOR);
                
            	
                ColectorEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (ColectorEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getClave(),
							obj.getCodINEProvincia(), 
							obj.getCodINEMunicipio(),
							obj.getCodOrden(), 
							obj.getTitularidad(), 
							obj.getGestion(),
							obj.getEstado(), 
							obj.getMaterial(),
							obj.getSist_impulsion(), 
							obj.getTipo_red(),
							obj.getTip_interceptor(), 
							obj.getFecha_inst(),
							obj.getObservaciones(),
							obj.getFechaRevision(), 
							obj.getEstado_Revision(),
							obj.getBloqueado() };
					rows.add(rowData);

				}
                
                ((ColectoresVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
                

                
            }
            else if (patron.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
                
                setNameTableModel(ConstantesLocalGISEIEL.AGRUPACIONES6000_MODEL_COMPLETO_VERSIONADO);
                
                refrescarPanel();
                getJPanelBotonera().getJButtonVerVersion().setEnabled(false);
                
                ArrayList lst = obtenerListaDatos(ConstantesLocalGISEIEL.AGRUPACIONES6000);
                
            	
                EntidadesAgrupadasEIEL obj;
				ArrayList rows = new ArrayList();
				for (int i = 0; i < lst.size(); i++) {
					obj = (EntidadesAgrupadasEIEL) lst.get(i);
					Object[] rowData = { 
							obj.getVersion().getIdVersion(),
							obj.getVersion().getAccion(),
							obj.getVersion().getUsuario(),
							obj.getVersion().getFecha(), 
							obj.getCodINEMunicipio(),
							obj.getCodEntidad(), 
							obj.getCodNucleo(), 
							obj.getCodEntidad_agrupada(),
							obj.getCodNucleo_agrupado()};
					rows.add(rowData);

				}
                
                ((EntidadesAgrupadasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).setData(rows);
                

                
            }
            else{
                getJPanelBotonera().setEnabled(false);
            }
            
            
          //en cualquier caso, ordenamos todos los campos de la tabla
            try{
            	int elementos=((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel().getRowCount();
            	if (elementos==0){
            		JOptionPane.showMessageDialog(this, "No existe ningun elemento que cumpla el filtro","Versionao", JOptionPane.INFORMATION_MESSAGE);
            	}
            }
            catch (Exception e){
            }
    		OrdenarTabla(((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()));
    		
    		
    		//Intentamos dejar de nuevo seleecionado el elemento que hemos obtenido al principio del método
//    		if (selectedItem != null){
//    			this.setSeletecdRowByItem(selectedItem);
//    		}
            
        }
        
    }
    
    private void refrescarPanel() {
//      panelPrincipal.remove(jPanelListaElementos);
//      jPanelListaElementos = null;
//      panelPrincipal.add(getJPanelListaElementos(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
//              GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
//      panelPrincipal.revalidate();
		//Se elimina la tabla para que se recargue
    	
    	getJPanel().remove(jPanelListaElementos);
		jPanelListaElementos = null;
        getJPanel().add(getJPanelListaElementos(),      new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        getJPanel().revalidate();
		
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

    				AbastecimientoAutonomoEIEL obj= ((AbastecimientoAutonomoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CaptacionesEIEL obj= ((CaptacionesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				Depuradora1EIEL obj= ((Depuradora1VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				Depuradora2EIEL obj= ((Depuradora2VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CasasConsistorialesEIEL obj= ((CasasConsistorialesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CentrosCulturalesEIEL obj= ((CentrosCulturalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CentrosEnsenianzaEIEL obj= ((CentrosEnsenianzaVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				InstalacionesDeportivasEIEL obj= ((InstalacionesDeportivasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				NucleosPoblacionEIEL obj= ((NucleosPoblacionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				OtrosServMunicipalesEIEL obj= ((OtrosServMunicipalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				OtrosServMunicipalesEIEL compareObject = (OtrosServMunicipalesEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

    				PadronNucleosEIEL obj= ((PadronNucleosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				PadronMunicipiosEIEL obj= ((PadronMunicipiosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				PadronMunicipiosEIEL compareObject = (PadronMunicipiosEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    						obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia())){
    					return obj;
    				}else{
    					return null;
    				}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

    				ParquesJardinesEIEL obj= ((ParquesJardinesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				PlaneamientoUrbanoEIEL obj= ((PlaneamientoUrbanoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				PoblamientoEIEL obj= ((PoblamientoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				RecogidaBasurasEIEL obj= ((RecogidaBasurasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CentrosSanitariosEIEL obj= ((CentrosSanitariosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				SaneamientoAutonomoEIEL obj= ((SaneamientoAutonomoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				ServiciosSaneamientoEIEL obj= ((ServiciosSaneamientoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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
//    				ServiciosRecogidaBasuraEIEL obj= ((ServiciosRecogidaBasuraVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				EdificiosSinUsoEIEL obj= ((EdificiosSinUsoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				TanatoriosEIEL obj= ((TanatoriosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				VertederosEIEL obj= ((VertederosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				TramosCarreterasEIEL obj= ((TramosCarreterasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				TramosCarreterasEIEL compareObject = (TramosCarreterasEIEL) selectedObject;

    				if (obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    						obj.getCodCarretera().equals(compareObject.getCodCarretera())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.DEPOSITOS)){

    				DepositosEIEL obj= ((DepositosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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
    			
    				PuntosVertidoEIEL obj= ((PuntosVertidoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				ServiciosAbastecimientosEIEL obj= ((ServiciosAbastecimientosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CentrosAsistencialesEIEL obj= ((CentrosAsistencialesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				CabildoConsejoEIEL obj= ((CabildoConsejoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				CabildoConsejoEIEL compareObject = (CabildoConsejoEIEL) selectedObject;
    				
    				if (obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) &&
    						obj.getCodIsla().equals(compareObject.getCodIsla())){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){

    				CementeriosEIEL obj= ((CementeriosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				EntidadesSingularesEIEL obj= ((EntidadesSingularesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				NucleoEncuestado7EIEL obj= ((InfoTerminosMunicipalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				IncendiosProteccionEIEL obj= ((IncendiosProteccionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				LonjasMercadosEIEL obj= ((LonjasMercadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				MataderosEIEL obj= ((MataderosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				TratamientosPotabilizacionEIEL obj= ((TratamientosPotabilizacionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				DiseminadosEIEL obj= ((DiseminadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
    				DiseminadosEIEL compareObject = (DiseminadosEIEL) selectedObject;
    				
    				if (obj.getCodINEMunicipio().equals(compareObject.getCodINEMunicipio()) && 
    							obj.getCodINEProvincia().equals(compareObject.getCodINEProvincia()) ){
    					return obj;
    				}else {
						return null;
					}
    			}
    			else if (patternSelected.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){

    				Encuestados1EIEL obj= ((Encuestados1VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				Encuestados2EIEL obj= ((Encuestados2VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				NucleosAbandonadosEIEL obj= ((NucleosAbandonadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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

    				EmisariosEIEL obj= ((EmisariosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
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
    
    private void listaelementosJPanel_actionPerformed(){
        /** Desde GIS. Si no se ha bloqueado la capa para hacer los cambios, lock es true (evitamos hacer cambios en la feature) */

        try {
            
            int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
        	
            if (patron != null && !patron.equals("") && selectedRow>=0){
            	if(patron.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){
              	  ((BotoneraVersionJPanel)getJPanelBotonera()).getJButtonVerVersion().setEnabled(false);
            	}else{
              	  ((BotoneraVersionJPanel)getJPanelBotonera()).getJButtonVerVersion().setEnabled(true);

            	}
            	((BotoneraVersionJPanel)getJPanelBotonera()).getJButtonFijarVersion().setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
        
    public Object getSelectedElement(){

        try {            
            
        	int selectedRow = ((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getSelectedRow();
        	
            if (patron != null && !patron.equals("") && selectedRow>= 0){

                if (patron.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)){


                    AbastecimientoAutonomoEIEL obj= ((AbastecimientoAutonomoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;

                }
                //MARKED
                else if (patron.equals(ConstantesLocalGISEIEL.CAPTACIONES)){

                    CaptacionesEIEL obj= ((CaptacionesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS1)){

                    Depuradora1EIEL obj= ((Depuradora1VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPURADORAS2)){

                    Depuradora2EIEL obj= ((Depuradora2VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)){

                    CasasConsistorialesEIEL obj= ((CasasConsistorialesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)){

                    CentrosCulturalesEIEL obj= ((CentrosCulturalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)){

                    CentrosEnsenianzaEIEL obj= ((CentrosEnsenianzaVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)){

                    InstalacionesDeportivasEIEL obj= ((InstalacionesDeportivasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)){

                    NucleosPoblacionEIEL obj= ((NucleosPoblacionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)){

                    OtrosServMunicipalesEIEL obj= ((OtrosServMunicipalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)){

                    PadronNucleosEIEL obj= ((PadronNucleosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)){

                    PadronMunicipiosEIEL obj= ((PadronMunicipiosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)){

                    ParquesJardinesEIEL obj= ((ParquesJardinesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)){

                    PlaneamientoUrbanoEIEL obj= ((PlaneamientoUrbanoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.POBLAMIENTO)){

                    PoblamientoEIEL obj= ((PoblamientoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)){

                    RecogidaBasurasEIEL obj= ((RecogidaBasurasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)){

                    CentrosSanitariosEIEL obj= ((CentrosSanitariosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)){

                    SaneamientoAutonomoEIEL obj= ((SaneamientoAutonomoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)){

                    ServiciosSaneamientoEIEL obj= ((ServiciosSaneamientoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)){

                    ServiciosRecogidaBasuraEIEL obj= ((ServiciosRecogidaBasuraVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)){

                    EdificiosSinUsoEIEL obj= ((EdificiosSinUsoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TANATORIOS)){

                    TanatoriosEIEL obj= ((TanatoriosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)){

                    VertederosEIEL obj= ((VertederosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)){

                    TramosCarreterasEIEL obj= ((TramosCarreterasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DEPOSITOS)){
                    DepositosEIEL obj= ((DepositosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)){

                    PuntosVertidoEIEL obj= ((PuntosVertidoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)){
                    
                    ServiciosAbastecimientosEIEL obj= ((ServiciosAbastecimientosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)){
                    
                    CentrosAsistencialesEIEL obj= ((CentrosAsistencialesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CABILDO)){
                    
                    CabildoConsejoEIEL obj= ((CabildoConsejoVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.CEMENTERIOS)){
                    
                    CementeriosEIEL obj= ((CementeriosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)){
                    
                    EntidadesSingularesEIEL obj= ((EntidadesSingularesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)){
                    
                    NucleoEncuestado7EIEL obj= ((InfoTerminosMunicipalesVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)){
                    
                    IncendiosProteccionEIEL obj= ((IncendiosProteccionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)){
                    
                    LonjasMercadosEIEL obj= ((LonjasMercadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.MATADEROS)){
                    
                    MataderosEIEL obj= ((MataderosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)){
                    
                    TratamientosPotabilizacionEIEL obj= ((TratamientosPotabilizacionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.DISEMINADOS)){
                    
                    DiseminadosEIEL obj= ((DiseminadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)){
                    
                    Encuestados1EIEL obj= ((Encuestados1VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)){
                    
                    Encuestados2EIEL obj= ((Encuestados2VersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)){
                    
                    NucleosAbandonadosEIEL obj= ((NucleosAbandonadosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.EMISARIOS)){

                    EmisariosEIEL obj= ((EmisariosVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TCONDUCCION)){

                    TramosConduccionEIEL obj= ((TramoConduccionVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.TCOLECTOR)){

                    ColectorEIEL obj= ((ColectoresVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
                else if (patron.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)){

                    EntidadesAgrupadasEIEL obj= ((EntidadesAgrupadasVersionadoCompletoEIELTableModel)((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).getTableModel()).getValueAt(getModelIndex(selectedRow));
                    return obj;
                }
            }
        } catch (Exception e) {
        	//El patron no coincide con el tipo de elemento seleccionado
            e.printStackTrace();
        }
        return null;

    }

	private void abrirDialogo(String selectedPattern, String tipoOperacion)
			throws Exception {

		Object selectedElement = getSelectedElement();

		if (selectedPattern==null){
			
		}
		else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.ABASTECIMIENTO_AUTONOMO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof AbastecimientoAutonomoEIEL) {
					AbastecimientoAutonomoDialog dialog = new AbastecimientoAutonomoDialog(
							(AbastecimientoAutonomoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof AbastecimientoAutonomoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(AbastecimientoAutonomoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
			//MARKED
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.CAPTACIONES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CaptacionesEIEL) {
					CaptacionesDialog dialog = new CaptacionesDialog(
							(CaptacionesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CaptacionesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CAPTACIONES_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CaptacionesEIEL) selectedElement,
										idLayer, selectedPattern);
					}

				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS1)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Depuradora1EIEL) {
					Depuradoras1Dialog dialog = new Depuradoras1Dialog(
							(Depuradora1EIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Depuradora1EIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(Depuradora1EIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPURADORAS2)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Depuradora2EIEL) {
					Depuradoras2Dialog dialog = new Depuradoras2Dialog(
							(Depuradora2EIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Depuradora2EIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.DEPURADORAS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(Depuradora2EIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.CASAS_CONSISTORIALES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CasasConsistorialesEIEL) {
					CasaConsistorialDialog dialog = new CasaConsistorialDialog(
							(CasasConsistorialesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CasasConsistorialesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CASAS_CONSISTORIALES_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CasasConsistorialesEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.CENTROS_CULTURALES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosCulturalesEIEL) {
					CentroCulturalDialog dialog = new CentroCulturalDialog(
							(CentrosCulturalesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosCulturalesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CENTROS_CULTURALES_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CentrosCulturalesEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.CENTROS_ENSENIANZA)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosEnsenianzaEIEL) {
					CentroEnsenianzaDialog dialog = new CentroEnsenianzaDialog(
							(CentrosEnsenianzaEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosEnsenianzaEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CENTROS_ENSENIANZA_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CentrosEnsenianzaEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof InstalacionesDeportivasEIEL) {
					InstalacionDeportivaDialog dialog = new InstalacionDeportivaDialog(
							(InstalacionesDeportivasEIEL) selectedElement,
							false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof InstalacionesDeportivasEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.INSTALACIONES_DEPORTIVAS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(InstalacionesDeportivasEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.NUCLEOS_POBLACION)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof NucleosPoblacionEIEL) {
					NucleosPoblacionDialog dialog = new NucleosPoblacionDialog(
							(NucleosPoblacionEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof NucleosPoblacionEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(NucleosPoblacionEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.OTROS_SERVICIOS_MUNICIPALES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof OtrosServMunicipalesEIEL) {
					OtrosServMunicipalesDialog dialog = new OtrosServMunicipalesDialog(
							(OtrosServMunicipalesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof OtrosServMunicipalesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(OtrosServMunicipalesEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.PADRON_NUCLEOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PadronNucleosEIEL) {
					PadronNucleosDialog dialog = new PadronNucleosDialog(
							(PadronNucleosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PadronNucleosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(PadronNucleosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.PADRON_MUNICIPIOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PadronMunicipiosEIEL) {
					PadronMunicipiosDialog dialog = new PadronMunicipiosDialog(
							(PadronMunicipiosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PadronMunicipiosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(PadronMunicipiosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.PARQUES_JARDINES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ParquesJardinesEIEL) {
					ParquesJardinesDialog dialog = new ParquesJardinesDialog(
							(ParquesJardinesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ParquesJardinesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.PARQUES_JARDINES_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(ParquesJardinesEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.PLANEAMIENTO_URBANO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PlaneamientoUrbanoEIEL) {
					PlaneamientoUrbanoDialog dialog = new PlaneamientoUrbanoDialog(
							(PlaneamientoUrbanoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PlaneamientoUrbanoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.MUNICIPIO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(PlaneamientoUrbanoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.POBLAMIENTO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PoblamientoEIEL) {
					PoblamientoDialog dialog = new PoblamientoDialog(
							(PoblamientoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PoblamientoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(PoblamientoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.RECOGIDA_BASURAS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof RecogidaBasurasEIEL) {
					RecogidaBasurasDialog dialog = new RecogidaBasurasDialog(
							(RecogidaBasurasEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof RecogidaBasurasEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(RecogidaBasurasEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.CENTROS_SANITARIOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosSanitariosEIEL) {
					CentrosSanitariosDialog dialog = new CentrosSanitariosDialog(
							(CentrosSanitariosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosSanitariosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CENTROS_SANITARIOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CentrosSanitariosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.SANEAMIENTO_AUTONOMO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof SaneamientoAutonomoEIEL) {
					SaneamientoAutonomoDialog dialog = new SaneamientoAutonomoDialog(
							(SaneamientoAutonomoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof SaneamientoAutonomoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(SaneamientoAutonomoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_SANEAMIENTO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ServiciosSaneamientoEIEL) {
					ServiciosSaneamientoDialog dialog = new ServiciosSaneamientoDialog(
							(ServiciosSaneamientoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ServiciosSaneamientoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(ServiciosSaneamientoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.SERVICIOS_RECOGIDA_BASURA)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ServiciosRecogidaBasuraEIEL) {
					ServiciosRecogidaBasurasDialog dialog = new ServiciosRecogidaBasurasDialog(
							(ServiciosRecogidaBasuraEIEL) selectedElement,
							false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ServiciosRecogidaBasuraEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(ServiciosRecogidaBasuraEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof EdificiosSinUsoEIEL) {
					EdificiosSinUsoDialog dialog = new EdificiosSinUsoDialog(
							(EdificiosSinUsoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof EdificiosSinUsoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.EDIFICIOS_SIN_USO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(EdificiosSinUsoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.TANATORIOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof TanatoriosEIEL) {
					TanatoriosDialog dialog = new TanatoriosDialog(
							(TanatoriosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof TanatoriosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.TANATORIO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(TanatoriosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.DATOS_VERTEDEROS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof VertederosEIEL) {
					VertederosDialog dialog = new VertederosDialog(
							(VertederosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof VertederosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.VERTEDERO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(VertederosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.TRAMOS_CARRETERAS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof TramosCarreterasEIEL) {
					CarreterasDialog dialog = new CarreterasDialog(
							(TramosCarreterasEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof TramosCarreterasEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CARRETERA_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(TramosCarreterasEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.DEPOSITOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof DepositosEIEL) {
					DepositosDialog dialog = new DepositosDialog(
							(DepositosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof DepositosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.DEPOSITOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(DepositosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.PUNTOS_VERTIDO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PuntosVertidoEIEL) {
					PuntosVertidoDialog dialog = new PuntosVertidoDialog(
							(PuntosVertidoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof PuntosVertidoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.EMISARIOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(PuntosVertidoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.DATOS_SERVICIOS_ABASTECIMIENTOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ServiciosAbastecimientosEIEL) {
					ServiciosAbastecimientosDialog dialog = new ServiciosAbastecimientosDialog(
							(ServiciosAbastecimientosEIEL) selectedElement,
							false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof ServiciosAbastecimientosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(ServiciosAbastecimientosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosAsistencialesEIEL) {
					CentrosAsistencialesDialog dialog = new CentrosAsistencialesDialog(
							(CentrosAsistencialesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CentrosAsistencialesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CENTROS_ASISTENCIALES_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CentrosAsistencialesEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.CABILDO)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CabildoConsejoEIEL) {
					CabildoConsejoDialog dialog = new CabildoConsejoDialog(
							(CabildoConsejoEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CabildoConsejoEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.PROVINCIA_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CabildoConsejoEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.CEMENTERIOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CementeriosEIEL) {
					CementeriosDialog dialog = new CementeriosDialog(
							(CementeriosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof CementeriosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.CEMENTERIOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(CementeriosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.ENTIDADES_SINGULARES)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof EntidadesSingularesEIEL) {
					EntidadesSingularesDialog dialog = new EntidadesSingularesDialog(
							(EntidadesSingularesEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof EntidadesSingularesEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(EntidadesSingularesEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.NUCLEO_ENCT_7)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof NucleoEncuestado7EIEL) {
					InfoTerminosMunicipalesDialog dialog = new InfoTerminosMunicipalesDialog(
							(NucleoEncuestado7EIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof NucleoEncuestado7EIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(NucleoEncuestado7EIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.INCENDIOS_PROTECCION)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof IncendiosProteccionEIEL) {
					IncendiosProteccionDialog dialog = new IncendiosProteccionDialog(
							(IncendiosProteccionEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof IncendiosProteccionEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.INCENDIOS_PROTECCION_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(IncendiosProteccionEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.LONJAS_MERCADOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof LonjasMercadosEIEL) {
					LonjasMercadosDialog dialog = new LonjasMercadosDialog(
							(LonjasMercadosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof LonjasMercadosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.LONJAS_MERCADOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(LonjasMercadosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.MATADEROS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof MataderosEIEL) {
					MataderosDialog dialog = new MataderosDialog(
							(MataderosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof MataderosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.MATADEROS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(MataderosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof TratamientosPotabilizacionEIEL) {
					TratamientosPotabilizacionDialog dialog = new TratamientosPotabilizacionDialog(
							(TratamientosPotabilizacionEIEL) selectedElement,
							false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof TratamientosPotabilizacionEIEL) {
					GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
							.getEditor().getLayerManager()
							.getLayer(ConstantesLocalGISEIEL.TRATAMIENTOS_POTABILIZACION_LAYER);
					String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
					InitEIEL.clienteLocalGISEIEL
							.insertarElemento(
									(TratamientosPotabilizacionEIEL) selectedElement,
									idLayer, selectedPattern);
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.DISEMINADOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof DiseminadosEIEL) {
					DiseminadosDialog dialog = new DiseminadosDialog(
							(DiseminadosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof DiseminadosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.MUNICIPIOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(DiseminadosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS1)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Encuestados1EIEL) {
					Encuestados1Dialog dialog = new Encuestados1Dialog(
							(Encuestados1EIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Encuestados1EIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(Encuestados1EIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern.equals(ConstantesLocalGISEIEL.ENCUESTADOS2)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Encuestados2EIEL) {
					Encuestados2Dialog dialog = new Encuestados2Dialog(
							(Encuestados2EIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof Encuestados2EIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(Encuestados2EIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		} else if (selectedPattern
				.equals(ConstantesLocalGISEIEL.NUCLEOS_ABANDONADOS)) {
			if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof NucleosAbandonadosEIEL) {
					NucleosAbandonadosDialog dialog = new NucleosAbandonadosDialog(
							(NucleosAbandonadosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion
					.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null
						&& selectedElement instanceof NucleosAbandonadosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL",
									"localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(
										ConstantesLocalGISEIEL.NUCLEO_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL
								.insertarElemento(
										(NucleosAbandonadosEIEL) selectedElement,
										idLayer, selectedPattern);
					}
				}
			}
		}
		else if (selectedPattern.equals(ConstantesLocalGISEIEL.EMISARIOS)) {
			if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null && selectedElement instanceof EmisariosEIEL) {
					EmisoresDialog dialog = new EmisoresDialog((EmisariosEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null && selectedElement instanceof EmisariosEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(ConstantesLocalGISEIEL.EMISARIOS_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL.insertarElemento((EmisariosEIEL) selectedElement,idLayer, selectedPattern);
					}
				}
			}
		} 
		else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCONDUCCION)) {
			if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null && selectedElement instanceof TramosConduccionEIEL) {
					TramosConduccionDialog dialog = new TramosConduccionDialog((TramosConduccionEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null && selectedElement instanceof TramosConduccionEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(ConstantesLocalGISEIEL.TRAMOS_CONDUCCIONES_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL.insertarElemento((TramosConduccionEIEL) selectedElement,idLayer, selectedPattern);
					}
				}
			}
		} 
		else if (selectedPattern.equals(ConstantesLocalGISEIEL.TCOLECTOR)) {
			if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null && selectedElement instanceof ColectorEIEL) {
					ColectoresDialog dialog = new ColectoresDialog((ColectorEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null && selectedElement instanceof ColectorEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(ConstantesLocalGISEIEL.COLECTOR_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL.insertarElemento((ColectorEIEL) selectedElement,idLayer, selectedPattern);
					}
				}
			}
		} 
		else if (selectedPattern.equals(ConstantesLocalGISEIEL.AGRUPACIONES6000)) {
			if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_VER_VERSION)) {
				if (selectedElement != null && selectedElement instanceof EntidadesAgrupadasEIEL) {
					EntidadesAgrupadasDialog dialog = new EntidadesAgrupadasDialog((EntidadesAgrupadasEIEL) selectedElement, false);
					dialog.setVisible(true);
				}
			} else if (tipoOperacion.equals(ConstantesLocalGISEIEL.OPERACION_FIJAR_VERSION)) {
				if (selectedElement != null && selectedElement instanceof EntidadesAgrupadasEIEL) {
					if (confirm(
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.mensaje"),
							I18N.get("LocalGISEIEL", "localgiseiel.register.fijarVersion.nombre"))) {
						GeopistaLayer geopistaLayer = (GeopistaLayer) GeopistaEditorPanel
								.getEditor()
								.getLayerManager()
								.getLayer(ConstantesLocalGISEIEL.AGRUPACIONES6000_LAYER);
						String idLayer = geopistaLayer!=null?geopistaLayer.getSystemId():null;
						InitEIEL.clienteLocalGISEIEL.insertarElemento((EntidadesAgrupadasEIEL) selectedElement,idLayer, selectedPattern);
					}
				}
			}
		} 
		
		//Debemos tener cargado panel de edicion para realizar operaciones de actualizacion
		if (EditingInfoPanel.getInstance() != null) {
			//Refrescamos el panel de ese modo actualiza el valor que ve el ususario en el interfaz.
			EditingInfoPanel.getInstance().listarDatosTabla();//Con este metodo el campor Versión de la lista sale completo
			//pero para que al usuario no le cambie el estilo de la lista se invoca al siguiente metodo que acorta ese campo y colorea los valores.
			EditingInfoPanel.getInstance().reColorearBloqueo(EditingInfoPanel.getInstance().getJPanelTree().getPatronSelected());
			//ya sea si el usuario le da a ver versión o a fijar versión los datos se actualizaran de ese modo nos evitamos tener que repetir el codigo en todos los fijar versión
			//y la aplicación no se relentiza haciedolo de este modo.
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
            
//          String filter = " order by versionesalfa.fecha desc ";
        	//String filter ="";
        	
        	String filter = " (revision_expirada!="+ConstantesLocalGISEIEL.REVISION_TEMPORAL;
    		filter += " and revision_expirada!="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE_MOVILIDAD;
    		filter += " and revision_expirada!="+ConstantesLocalGISEIEL.REVISION_BORRABLE;	
    		filter += " and revision_expirada!="+ConstantesLocalGISEIEL.REVISION_PUBLICABLE+") ";	
    		
    		
        	if (fechaVersionInicio!=null && !fechaVersionInicio.equalsIgnoreCase(""))
        		filter += " and versionesalfa.fecha > '" +fechaVersionInicio+"'";
        	
        	if (fechaVersionFin!=null && !fechaVersionFin.equalsIgnoreCase(""))
        		filter += " and versionesalfa.fecha < '" +fechaVersionFin+"'";
        	
        	if (accionVersion!=null && !accionVersion.equalsIgnoreCase(""))
        		filter += " AND versionesalfa.tipocambio like '" +accionVersion+"'";
        		
        	if (usuarioVersion!=null && !usuarioVersion.equalsIgnoreCase(""))
        		filter += " AND versionesalfa.id_autor = " +usuarioVersion;
        	
            lstElements = InitEIEL.clienteLocalGISEIEL.getLstElementosVersionados(filter, tipo,this.elemento,false);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstElements;
    }
    
    private ArrayList<Usuario> obtenerListaUsuarios(int idEntidad,int idEntidadOriginal){

    	ArrayList<Usuario> lstElements = new ArrayList();
        try {
             if(idEntidad>0) 
            	 lstElements = InitEIEL.clienteLocalGISEIEL.getLstUsuariosEntidad(String.valueOf(idEntidad),String.valueOf(idEntidadOriginal));
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstElements;
    }
    
    
    private int getModelIndex(int index){
        return ((TableSorted)((ListaDatosEIELJPanel) getJPanelListaElementos()).getJTableListaDatos().getModel()).modelIndex(index);
    }
    
    class ObjetoComboUser{
    	
    	private String nombre;
    	private String id;
    	
    	public ObjetoComboUser(String nombre, String id){
    		this.setNombre(nombre);
    		this.setId(id);
    	}
    	
    	public String toString(){
    		return getNombre();
    	}

    	/**
    	 * @param nombre the nombre to set
    	 */
    	public void setNombre(String nombre) {
    		this.nombre = nombre;
    	}

    	/**
    	 * @return the nombre
    	 */
    	public String getNombre() {
    		return nombre;
    	}

    	/**
    	 * @param tipo the tipo to set
    	 */
    	public void setId(String id) {
    		this.id = id;
    	}

    	/**
    	 * @return the tipo
    	 */
    	public String getId() {
    		return id;
    	}
    		
    }    
    
class ObjetoCombo implements Comparable{
	
	private String nombre;
	private String tipo;
	
	public ObjetoCombo(String nombre, String tipo){
		this.setNombre(nombre);
		this.setTipo(tipo);
	}
	
	public String toString(){
		return getNombre();
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	
	
    public int compareTo(Object o) {
       ObjetoCombo obj = (ObjetoCombo) o;
        return this.nombre.compareTo(obj.getNombre());
    }
		
	}
	
}
