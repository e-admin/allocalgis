package com.geopista.ui.plugin.infcattitularidad.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;
import com.geopista.app.catastro.intercambio.edicion.ExpedientePanelTree;
import com.geopista.app.catastro.intercambio.edicion.ParajesSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.RepartoExtendedInfoDialog;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.utils.ComunidadBienesCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.HideableNode;
import com.geopista.app.catastro.intercambio.edicion.utils.TableBienInmuebleCatastroModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableCultivoModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableFincaCatastroModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TableLocalModel;
import com.geopista.app.catastro.intercambio.edicion.utils.TablePersonaModel;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.BienInmuebleJuridico;
import com.geopista.app.catastro.model.beans.BienInmuebleCatastro;
import com.geopista.app.catastro.model.beans.ComunidadBienes;
import com.geopista.app.catastro.model.beans.ConstantesRegExp;
import com.geopista.app.catastro.model.beans.ConstruccionCatastro;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.Derecho;
import com.geopista.app.catastro.model.beans.DireccionLocalizacion;
import com.geopista.app.catastro.model.beans.ElementoReparto;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.FincaCatastro;
import com.geopista.app.catastro.model.beans.IdBienInmueble;
import com.geopista.app.catastro.model.beans.IdCultivo;
import com.geopista.app.catastro.model.beans.Paraje;
import com.geopista.app.catastro.model.beans.Persona;
import com.geopista.app.catastro.model.beans.ReferenciaCatastral;
import com.geopista.app.catastro.model.beans.RepartoCatastro;
import com.geopista.app.catastro.model.beans.SueloCatastro;
import com.geopista.app.catastro.model.beans.TipoExpediente;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.catastro.model.beans.UnidadConstructivaCatastro;
import com.geopista.app.catastro.model.datos.fisicos.DatosFisicosFinca;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistroExp;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TableSorted;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.comboBoxTipo.ComboBoxKeyTipoDestinoSelectionManager;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.ui.plugin.infcatfisicoeconomico.paneles.InfCatatralFisicoEconomicoPanel;
import com.geopista.ui.plugin.infcattitularidad.edicion.BienInmuebleExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.edicion.CultivoComunExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.edicion.CultivoExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.edicion.FincaExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.edicion.LocalComunExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.edicion.LocalExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.edicion.RazonSocialExtendedInfoDialog;
import com.geopista.ui.plugin.infcattitularidad.paneles.InfCatatralTitularidadPanel;
import com.geopista.ui.plugin.infcattitularidad.utils.EdicionUtils;
import com.geopista.util.FeatureExtendedPanel;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;


public class FincaPanel extends JPanel implements FeatureExtendedPanel
{
    private JPanel jPanelDatosFinca = null;
    private JPanel jPanelBienInmueble = null;
    private JLabel jLabelTipoCodigoVia = null;
    private JLabel jLabelTipoVia = null;
    private JLabel jLabelVia = null;
    private JLabel jLabelNumero = null;
    private JLabel jLabelAnioPonencia = null;
    private JTextField jTextFieldCodigoVia;
    private ComboBoxEstructuras jComboBoxTipoVia = null;
    private JTextField jTextFieldVia = null;
    private JButton jButtonBuscarVia = null;
    private JComboBox jComboBoxAnioPonencia = null;
    private JTextField jTextFieldNumero = null;
    private JLabel jLabelPoligono = null;
    private JLabel jLabelParcela = null;
    private JLabel jLabelParaje = null;
    private JLabel jLabelSupSolar = null;
    private JLabel jLabelSupTotal = null;
    private JLabel jLabelSupSobre = null;
    private JLabel jLabelSupCubierta = null;
    private JComboBox jComboBoxPoligono = null;
    private JTextField jTextFieldParcela = null;
    private JTextField jTextFieldParaje = null;
    private JButton jButtonBuscarParaje = null;
    private JTextField jTextFieldSupSolar = null;
    private String SupBajoRasante = null;
    private JTextField jTextFieldSupTotal = null;
    private JTextField jTextFieldSupSobre = null;
    private JTextField jTextFieldSupCubierta = null;
    private JLabel jLabelBloque = null;
    private JLabel jLabelEscalera = null;
    private JLabel jLabelPlanta = null;
    private JLabel jLabelPuerta = null;
    private JLabel jLabelNumOrdenDH = null;
    private JLabel jLabelCoefParticipacion = null;
    private JTextField jTextFieldBloque = null;
    private JTextField jTextFieldNumOrdenDH = null;
    private JTextField jTextFieldCoefParticipacion = null;
    private JPanel jPanelLocal = null;
    private JPanel jPanelTitular;
    private JPanel jPanelBotoneraLocal;
    private JButton jButtonEliminarLocal;
    private JButton jButtonNuevoLocal;
    private JButton jButtonNuevoLocalComun;
    private JButton jButtonNuevoCultivoComun;
    private JButton jButtonVistaCultivo;
    private JButton jButtonMasDatosLocales;
    private JPanel jPanelBotoneraFinca;
    private JButton jButtonEliminarFinca;
    private JButton jButtonNuevaFinca;
    private JButton jButtonMasDatosFinca;
    private JButton jButtonMasDatosCultivo;
    private JPanel jPanelBotoneraTitular;
    private JButton jButtonNuevoTitular;
    private JButton jButtonMasDatosTitular;
    private JButton jButtonEliminarTitular;
    private JLabel jLabelRazonSocial = null;
    private JTextField jTextFieldRazonSocial = null;
    private JLabel jLabelDerecho = null;
    private JLabel jLabelPorcentaje = null;
    private JLabel jLabelNifConyuge = null;
    private JPanel jPanelComunidadBienes = null;
    private JComboBox jComboBoxComunidadBienes = null;
    private JTextField jTextFieldComunidadBienes = null;
    private JButton jButtonNueModComunidadBienes = null;
    private ComboBoxEstructuras jComboBoxDerecho = null;
    private JTextField jTextFieldPorcentaje = null;
    private JTextField jTextFieldNifConyuge = null;
    private JPanel jPanelBotoneraBienInmueble;
    private JButton jButtonEliminarInmueble;
    private JButton jButtonNuevoInmueble;
    private JButton jButtonDefinirRepresentante;
    private JButton jButtonMasDatosInmueble;
    private JPanel jPanelBuscarVia;
    private JLabel jLabelNIF;
    private JTextField jTextFieldNif;
    private JLabel jLabelDC2;
    private JLabel jLabelDC1;
    private JLabel jLabelCargo;
    private JLabel jLabelPCatastral2;
    private JLabel jLabelPCatastral1;
    private JTextField jTextFieldDC2;
    private JTextField jTextFieldDC1;
    private JTextField jTextFieldCargo;
    private JTextField jTextFieldPCatastral2;
    private JTextField jTextFieldPCatastral1;
    private JLabel jLabelRefCatastral1;
    private JLabel jLabelRefCatastral2;
    private JTextField jTextFieldRefCatastral2;
    private JTextField jTextFieldRefCatastral1;
    private JPanel jPanelRefCatastralFinca;
    private JPanel jPanelRefCatastralBien;
    private JPanel jPanelDatosLocal = null;
    private JLabel jLabelIdLocal = null;
    private JLabel jLabelUnidadConst = null;
    private JLabel jLabelBloqueLocal = null;
    private JLabel jLabelEscaleraLocal = null;
    private JLabel jLabelPlantaLocal = null;
    private JLabel jLabelPuertaLocal = null;
    private JLabel jLabelDestinoLocal = null;
    private JTextField jTextFieldIdLocal = null;
    private JTextField jTextFieldUnidadConstrLocal = null;
    private JTextField jTextFieldBloqueLocal = null;
    private JComboBox jComboBoxDestinoLocal = null;
    private JLabel jLabelSupTotalLocal = null;
    private JLabel jLabelSupTerrazaLocal = null;
    private JLabel jLabelTipologia = null;
    private JLabel jLabelCategoria = null;
    private JLabel jLabelCodTipoValor = null;
    private JTextField jTextFieldSupTotalLocal = null;
    private JTextField jTextFieldSupTerrazaLocal = null;
    private JComboBox jComboBoxTipologiaLocal = null;
    private ComboBoxEstructuras jComboBoxCategoria = null;
    private ComboBoxEstructuras jComboBoxCodTipoValor = null;
    private JButton jButtonComunidadBienes = null;    
    private JPanel jPanelBotoneraCultivo = null;
    private JPanel jPanelDatosCultivo = null;
    private JLabel jLabelSubparcela = null;
    private JLabel jLabelTipoCultivo = null;
    private JComboBox jComboBoxTipoCultivo = null;
    private JLabel jLabelTipoSubparcela = null;
    private ComboBoxEstructuras jComboBoxTipoSubparcela = null;
    private JLabel jLabelIntensidadProductiva = null;
    private JComboBox jComboBoxIntensidadProductiva = null;
    private JLabel jLabelSuperficie = null;
    private JTextField jTextFieldSuperficieCultivo = null;
    private JButton jButtonReparto = null;
    private JButton jButtonVistaLocal = null;
    private JButton jButtonNuevoCultivo = null;
    private JButton jButtonEliminarCultivo = null;
    
    
    private static AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard blackboard = app.getBlackboard();
    
    private JTextField jTextFieldSubparcela;
    private JPanel jPanelCultivo;
    private JPanel jPanel;
    
    //private FincaCatastro fincaEditada = new FincaCatastro();
    private FincaCatastro fincaEditada = null;
    private BienInmuebleJuridico bienInmuebleJuridico = new BienInmuebleJuridico();  //  @jve:decl-index=0:
    private Persona representante = new Persona();
    private Cultivo cultivo = new Cultivo();
    private ConstruccionCatastro construccion = new ConstruccionCatastro();
    private Titular titular = new Titular();
    private JScrollPane jScrollPaneBienInmueble;
    private JTable jTableBienInmueble;
    private ComunidadBienes comunidadbienes;
    
    private TableFincaCatastroModel tablefincamodel;
    private TableBienInmuebleCatastroModel tablebimodel;
    private TableLocalModel tablelocalmodel;
    private TableCultivoModel tablecultivomodel;
    private TablePersonaModel tablepersonamodel;
    private JPanel jPanelDatosBienInmueble;
    private JScrollPane jScrollPaneTitular;
    private JTable jTableTitular;
    private JPanel jPanelDatosTitular;
    private JTable jTableLocal;
    private JTable jTableCultivo;
    private JScrollPane jScrollPaneLocal;
    private JScrollPane jScrollPaneCultivo;
    private boolean isEditable = true;
	private JLabel jLabelFechaAlteracion = null;
	private JTextField jTextFieldFechaAlteracion = null;
	private JLabel jLabelFechaAlteracionTitular = null;
	private JTextField jTextFieldFechaAlteracionTitular = null;
	private JLabel jLabelClase = null;
	//private JComboBox jComboBoxClase = null;
	private ComboBoxEstructuras jComboBoxClase = null;
	private JComboBox jComboBoxEscalera = null;
	private JComboBox jComboBoxPlanta = null;
	private JComboBox jComboBoxPuerta = null;
	private JComboBox jComboBoxEscaleraLocal = null;
	private JComboBox jComboBoxPlantaLocal = null;
	private JComboBox jComboBoxPuertaLocal = null; 
    
    //private Object ultimoNodo;
    private JTree arbol;
    private String focoEn;
    private ExpedientePanelTree panelExpedientes;
    private JComponent componenteConFoco;
    private boolean focoAutomatico = false;
    private boolean focoManual = false;
	private JScrollPane jScrollPaneFincas = null;
	private JTable jTableFincas = null;
	private JPanel jPanelFinca = null;
	private JPanel jPanelBienInmuebleGlobal = null;
	
	private Paraje paraje = null;
	private TipoExpediente tipoExpediente = null;
	private GestionExpedientePanel gestionExpedientePanel = null;
    
    public FincaPanel()
    {
        super();
        initialize();       
        
    }
    
    public FincaPanel(TipoExpediente tipoExpediente)
    {
        super();
        this.tipoExpediente  = tipoExpediente;
        initialize();
    }
    
    public FincaPanel(TipoExpediente tipoExpediente, GestionExpedientePanel gestionExpedientePanel)
    {
        super();
        this.gestionExpedientePanel = gestionExpedientePanel;
        this.tipoExpediente  = tipoExpediente;
        initialize();
    }
    
    public FincaPanel(FincaCatastro finca)
    {
        super();
        initialize();       
        loadData (finca);
        
    }
    
    public FincaPanel(BienInmuebleCatastro bic)
    {
        super();
        initialize();       
        
        loadData (bic);
        
    }
    
    
    private void initialize()
    {
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
        
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(848, 500));
        this.setPreferredSize(new java.awt.Dimension(450,500));
        this.add(getJPanelFinca(),
                new GridBagConstraints(0,0,1,1, 0.1, 0.1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
                        new Insets(0,5,0,5),0,0));
        this.add(getJPanelBienInmueble(), 
                new GridBagConstraints(0,1,1,1, 0.1, 0.1,
                        GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 
                        new Insets(0,5,0,5),0,0));
        
        initComboBoxes();
        focoPanelPantalla();
        focoPanelBI();
        focoPanelLocal();
        focoEnPersona();
    }
    
    private void initComboBoxes()
    {
        //Inicializa los desplegables        
    	if (blackboard.get("ListaTipoDestino")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerTiposDestino();
            blackboard.put("ListaTipoDestino", lst);
            EdicionUtils.cargarLista(getJComboBoxDestinoLocal(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxDestinoLocal(), 
                    (ArrayList)blackboard.get("ListaTipoDestino"));
        } 
        
        if (blackboard.get("ListaTipologiaLocal")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerTipologiasLocales();
            blackboard.put("ListaTipologiaLocal", lst);
            EdicionUtils.cargarLista(getJComboBoxTipologiaLocal(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxTipologiaLocal(), 
                    (ArrayList)blackboard.get("ListaTipologiaLocal"));
        }        
        
        if (blackboard.get("ListaCategoriaCultivo")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerCategoriaCultivo();
            blackboard.put("ListaCategoriaCultivo", lst);
            EdicionUtils.cargarLista(getJComboBoxTipoCultivo(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxTipoCultivo(), 
                    (ArrayList)blackboard.get("ListaCategoriaCultivo"));
        } 
        
        if (blackboard.get("ListaEscalera")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerEscalera();
            blackboard.put("ListaEscalera", lst);
            EdicionUtils.cargarLista(getJComboBoxEscalera(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxEscalera(), 
                    (ArrayList)blackboard.get("ListaEscalera"));
        } 
        
        if (blackboard.get("ListaPlanta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPlanta();
            blackboard.put("ListaPlanta", lst);
            EdicionUtils.cargarLista(getJComboBoxPlanta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPlanta(), 
                    (ArrayList)blackboard.get("ListaPlanta"));
        } 
        
        if (blackboard.get("ListaPuerta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPuerta();
            blackboard.put("ListaPuerta", lst);
            EdicionUtils.cargarLista(getJComboBoxPuerta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPuerta(), 
                    (ArrayList)blackboard.get("ListaPuerta"));
        } 
        
        if (blackboard.get("ListaAnioPonencia")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerAnioPonencia();
            blackboard.put("ListaAnioPonencia", lst);
            EdicionUtils.cargarLista(getJComboBoxAnioPonencia(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxAnioPonencia(), 
                    (ArrayList)blackboard.get("ListaAnioPonencia"));
        }      
        
        if (blackboard.get("ListaPoligonos")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPoligonos();
            blackboard.put("ListaPoligonos", lst);
            EdicionUtils.cargarLista(getJComboBoxPoligono(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPoligono(), 
                    (ArrayList)blackboard.get("ListaPoligonos"));
        } 
        
        if (blackboard.get("ListaEscalera")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerEscalera();
            blackboard.put("ListaEscalera", lst);
            EdicionUtils.cargarLista(getJComboBoxEscaleraLocal(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxEscaleraLocal(), 
                    (ArrayList)blackboard.get("ListaEscalera"));
        } 
        
        if (blackboard.get("ListaPlanta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPlanta();
            blackboard.put("ListaPlanta", lst);
            EdicionUtils.cargarLista(getJComboBoxPlantaLocal(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPlantaLocal(), 
                    (ArrayList)blackboard.get("ListaPlanta"));
        } 
        
        if (blackboard.get("ListaPuerta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPuerta();
            blackboard.put("ListaPuerta", lst);
            EdicionUtils.cargarLista(getJComboBoxPuertaLocal(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPuertaLocal(), 
                    (ArrayList)blackboard.get("ListaPuerta"));
        } 
    }
    
    public void loadData(Object o)
    {
    	EdicionOperations oper = new EdicionOperations();
        ArrayList lst = new ArrayList();
        
        if (o instanceof FincaCatastro)
        {
           
        	this.fincaEditada = (FincaCatastro)o;
            
            //Datos finca
        	if(((FincaCatastro)o).getDatosExpediente().getFechaAlteracion()!=null){
        		if(((FincaCatastro)o).getDatosExpediente().getFechaAlteracion().getTime()!=0){
        			DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        			String fecha = df.format(((FincaCatastro)o).getDatosExpediente().getFechaAlteracion());
        			jTextFieldFechaAlteracion.setText(fecha);
        		}
        		else{
        			jTextFieldFechaAlteracion.setText("");
        		}
        	}
        	else{
        		jTextFieldFechaAlteracion.setText("");
        	}
            jTextFieldRefCatastral1.setText(((FincaCatastro)o).getRefFinca().getRefCatastral1());
            jTextFieldRefCatastral2.setText(((FincaCatastro)o).getRefFinca().getRefCatastral2());
            
            jComboBoxTipoVia.setSelectedPatron(((FincaCatastro)o).getDirParcela().getTipoVia());
            Integer codigoVia =  new Integer( (((FincaCatastro)o).getDirParcela().getCodigoVia())); 
            jTextFieldCodigoVia.setText(codigoVia.toString());
            jTextFieldVia.setText(((FincaCatastro)o).getDirParcela().getNombreVia());
            
            if (((FincaCatastro)o).getDirParcela().getPrimerNumero() == -1){
            	jTextFieldNumero.setText("");
            }
            else{
            	jTextFieldNumero.setText(EdicionUtils.getStringValue(((FincaCatastro)o).getDirParcela().getPrimerNumero()));
            }
            
            if(((FincaCatastro)o).getDatosEconomicos().getAnioAprobacion()!=null){
            	if(((FincaCatastro)o).getDatosEconomicos().getAnioAprobacion().intValue()!=0){
            		jComboBoxAnioPonencia.setSelectedItem((EdicionUtils.getStringValue(((FincaCatastro)o).getDatosEconomicos().getAnioAprobacion())));            
            	}
            	else{
            		jComboBoxAnioPonencia.setSelectedIndex(0);
            	}
            }
            else{
            	jComboBoxAnioPonencia.setSelectedIndex(0);
            }
            if(((FincaCatastro)o).getDirParcela().getCodPoligono()!=null){
            	if(!((FincaCatastro)o).getDirParcela().getCodPoligono().equals("")){
            		jComboBoxPoligono.setSelectedItem(((FincaCatastro)o).getDirParcela().getCodPoligono());
            	}
            	else{
            		jComboBoxPoligono.setSelectedIndex(0);
            	}
            }
            else{
            	jComboBoxPoligono.setSelectedIndex(0);
            }
            jTextFieldParcela.setText(((FincaCatastro)o).getDirParcela().getCodParcela());
            //jTextFieldParaje.setText(((FincaCatastro)o).getDirParcela().getCodParaje());
            jTextFieldParaje.setText(((FincaCatastro)o).getDirParcela().getNombreParaje());
            
            if(paraje!=null){
            	paraje.setCodigo(((FincaCatastro)o).getDirParcela().getCodParaje());
            	paraje.setNombre(((FincaCatastro)o).getDirParcela().getNombreParaje());
            }
            jTextFieldSupSolar.setText(EdicionUtils.getStringValue(((FincaCatastro)o).getDatosFisicos().getSupFinca()));
            jTextFieldSupTotal.setText(EdicionUtils.getStringValue(((FincaCatastro)o).getDatosFisicos().getSupTotal()));
            jTextFieldSupSobre.setText(EdicionUtils.getStringValue(((FincaCatastro)o).getDatosFisicos().getSupSobreRasante()));
            jTextFieldSupCubierta.setText(EdicionUtils.getStringValue(((FincaCatastro)o).getDatosFisicos().getSupCubierta()));
            //JAVIER No se muestre en este panel pero hay que mantener el dato de supBajoRasante
            SupBajoRasante = EdicionUtils.getStringValue(((FincaCatastro)o).getDatosFisicos().getSupBajoRasante());
            
        }       
        else if (o instanceof BienInmuebleJuridico){        	
           
            this.bienInmuebleJuridico = (BienInmuebleJuridico)o;
            
            //datos bien inmueble
            jTextFieldPCatastral1.setText(bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral1());
            jTextFieldPCatastral2.setText(bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getParcelaCatastral().getRefCatastral2());
            jTextFieldCargo.setText(bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getNumCargo());
            jTextFieldDC1.setText(bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getDigControl1());
            jTextFieldDC2.setText(bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getDigControl2());
            jTextFieldBloque.setText(bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getBloque());
            
            EstructuraDB eEscalera = new EstructuraDB();
            if((jComboBoxEscalera.getItemCount() > 0) && (bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getEscalera()!=null)){
                lst = (ArrayList)blackboard.get("ListaEscalera");
                eEscalera = obtenerElementoLista(lst,bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getEscalera());
            	jComboBoxEscalera.setSelectedItem(eEscalera);            	
            }
            else{
            	jComboBoxEscalera.setSelectedIndex(0);
            }
            
            EstructuraDB ePlanta = new EstructuraDB();
            if((jComboBoxPlanta.getItemCount() > 0) && (bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getPlanta()!=null)){
            	lst = (ArrayList)blackboard.get("ListaPlanta");
            	ePlanta = obtenerElementoLista(lst, bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getPlanta());
                jComboBoxPlanta.setSelectedItem(ePlanta);
            }
            else{
            	jComboBoxPlanta.setSelectedIndex(0);
            }
            
            EstructuraDB ePuerta = new EstructuraDB();
            if((jComboBoxPuerta.getItemCount() > 0) && (bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getPuerta()!=null)){
            	lst = (ArrayList)blackboard.get("ListaPuerta");
            	ePuerta = obtenerElementoLista(lst, bienInmuebleJuridico.getBienInmueble().getDomicilioTributario().getPuerta());            	
            	jComboBoxPuerta.setSelectedItem(ePuerta);
            }
            else{
            	jComboBoxPuerta.setSelectedIndex(0);
            }
            
            jTextFieldNumOrdenDH.setText(bienInmuebleJuridico.getBienInmueble().getDatosEconomicosBien().getNumOrdenHorizontal());
            jTextFieldCoefParticipacion.setText(EdicionUtils.getStringValue(bienInmuebleJuridico.getBienInmueble().getDatosEconomicosBien().getCoefParticipacion()));
            
            
            loadComunidadBienes((BienInmuebleJuridico)o);
            
            
        }
        else if (o instanceof ConstruccionCatastro)
        {
            this.construccion = (ConstruccionCatastro)o;
            
            //local
            NumberFormat formatter = new DecimalFormat("0000");
                     
            //jTextFieldIdLocal.setText(((ConstruccionCatastro)o).getNumOrdenConstruccion());
            jTextFieldIdLocal.setText(((ConstruccionCatastro)o).getNumOrdenConstruccion()!=null?
            		formatter.format(new Integer(((ConstruccionCatastro)o).getNumOrdenConstruccion())).toString():"");
            jTextFieldUnidadConstrLocal.setText(((ConstruccionCatastro)o).getDatosFisicos().getCodUnidadConstructiva());
            jTextFieldBloqueLocal.setText(((ConstruccionCatastro)o).getDomicilioTributario().getBloque());
            
            EstructuraDB eEscalera = new EstructuraDB();
            if((jComboBoxEscaleraLocal.getItemCount() > 0) && (((ConstruccionCatastro)o).getDomicilioTributario().getEscalera()!=null)){
            	lst = (ArrayList)blackboard.get("ListaEscalera");
            	eEscalera = obtenerElementoLista(lst, ((ConstruccionCatastro)o).getDomicilioTributario().getEscalera());            	
                jComboBoxEscaleraLocal.setSelectedItem(eEscalera);
            }
            else{
            	jComboBoxEscaleraLocal.setSelectedIndex(0);
            }
            
            EstructuraDB ePlanta = new EstructuraDB();
            if((jComboBoxPlantaLocal.getItemCount() > 0) && (((ConstruccionCatastro)o).getDomicilioTributario().getPlanta()!=null)){
            	lst = (ArrayList)blackboard.get("ListaPlanta");
            	ePlanta = obtenerElementoLista(lst, ((ConstruccionCatastro)o).getDomicilioTributario().getPlanta());
                jComboBoxPlantaLocal.setSelectedItem(ePlanta);
            }
            else{
            	jComboBoxPlantaLocal.setSelectedIndex(0);
            }
            
            EstructuraDB ePuerta = new EstructuraDB();
            if((jComboBoxPuertaLocal.getItemCount() > 0) && (((ConstruccionCatastro)o).getDomicilioTributario().getPuerta()!=null)){
            	lst = (ArrayList)blackboard.get("ListaPuerta");
            	ePuerta = obtenerElementoLista(lst, ((ConstruccionCatastro)o).getDomicilioTributario().getPuerta());
            	jComboBoxPuertaLocal.setSelectedItem(ePuerta);
            }
            else{
            	jComboBoxPuertaLocal.setSelectedIndex(0);
            }
            
            EstructuraDB eDestino = new EstructuraDB();
            if((jComboBoxDestinoLocal.getItemCount() > 0) && (((ConstruccionCatastro)o).getDatosFisicos().getCodDestino()!=null)){
            	lst = (ArrayList)blackboard.get("ListaTipoDestino");
            	eDestino = obtenerElementoLista(lst, ((ConstruccionCatastro)o).getDatosFisicos().getCodDestino());
            	jComboBoxDestinoLocal.setSelectedItem(eDestino);
            }
            else
            {
                jComboBoxDestinoLocal.setSelectedIndex(0);                
            }
            
            jTextFieldSupTotalLocal.setText(EdicionUtils.getStringValue(((ConstruccionCatastro)o).getDatosFisicos().getSupTotal()));
            jTextFieldSupTerrazaLocal.setText(EdicionUtils.getStringValue(((ConstruccionCatastro)o).getDatosFisicos().getSupTerrazasLocal()));
            
            EstructuraDB eTipologia = new EstructuraDB();
            if((jComboBoxTipologiaLocal.getItemCount() > 0) && (((ConstruccionCatastro)o).getDatosEconomicos().getTipoConstruccion()!=null)){
            	lst = (ArrayList)blackboard.get("ListaTipologiaLocal");
            	eTipologia = obtenerElementoLista(lst, ((ConstruccionCatastro)o).getDatosEconomicos().getTipoConstruccion());
            	jComboBoxTipologiaLocal.setSelectedItem(eTipologia);
            }
            else
            {
                jComboBoxTipologiaLocal.setSelectedIndex(0);                
            }
            
            jComboBoxCategoria.setSelectedPatron(((ConstruccionCatastro)o).getDatosEconomicos().getCodCategoriaPredominante());
            jComboBoxCodTipoValor.setSelectedPatron(((ConstruccionCatastro)o).getDatosEconomicos().getCodTipoValor());
            
        }
        else if (o instanceof Cultivo)
        {
            this.cultivo = (Cultivo)o;
            
            if (((Cultivo)o).getCodSubparcela()!=null)
            	jTextFieldSubparcela.setText(((Cultivo)o).getCodSubparcela());
            
            EstructuraDB eTipoCultivo = new EstructuraDB();
            if((jComboBoxTipoCultivo.getItemCount() > 0) && (((Cultivo)o).getIdCultivo().getCalifCultivo()!=null)){
            	lst = (ArrayList)blackboard.get("ListaCategoriaCultivo");
            	eTipoCultivo = obtenerElementoLista(lst, ((Cultivo)o).getIdCultivo().getCalifCultivo());
            	jComboBoxTipoCultivo.setSelectedItem(eTipoCultivo);
            }
            else{
            	jComboBoxTipoCultivo.setSelectedIndex(0);
            }
           
            jComboBoxTipoSubparcela.setSelectedPatron(((Cultivo)o).getTipoSubparcela());
            if (jComboBoxTipoSubparcela.getSelectedItem()==null)
            {
            	jComboBoxTipoSubparcela.setSelectedIndex(0);                
            }
            
            NumberFormat formatter = new DecimalFormat("00");
            if(((Cultivo)o).getIntensidadProductiva()!=null){
            	jComboBoxIntensidadProductiva.setSelectedItem(formatter.format(new Integer(((Cultivo)o).getIntensidadProductiva().toString())));
            	if (jComboBoxIntensidadProductiva.getSelectedItem()==null)
            	{
            		jComboBoxIntensidadProductiva.setSelectedIndex(0);                
            	}
            }
            else{
            	jComboBoxIntensidadProductiva.setSelectedIndex(0); 
            }

            if (((Cultivo)o).getSuperficieParcela()!=null){
            	jTextFieldSuperficieCultivo.setText(EdicionUtils.getStringValue(((Cultivo)o).getSuperficieParcela()));
            }
            else{
            	jTextFieldSuperficieCultivo.setText("");
            }
            
            /*if(((Cultivo)o).getDenominacionCultivo()!=null)
            	jComboBoxClase.setSelectedItem(((Cultivo)o).getDenominacionCultivo());
            else
            	jComboBoxClase.setSelectedIndex(0);*/
            if(((Cultivo)o).getTipoSuelo()!=null)
            	//jComboBoxClase.setSelectedItem(((Cultivo)o).getTipoSuelo());
            	jComboBoxClase.setSelectedPatron(((Cultivo)o).getTipoSuelo());
            else
            	jComboBoxClase.setSelectedIndex(0);

        }
        
        else if (o instanceof Persona)
        {
            if (o instanceof Titular)
            {
                
                this.titular = (Titular)o;
                //Datos del Titular
                jTextFieldNif.setText(((Titular)o).getNif());
                jTextFieldRazonSocial.setText(((Titular)o).getRazonSocial());
                
                if(((Titular)o).getFechaAlteracion()!=null){
            		if(!((Titular)o).getFechaAlteracion().equals("")){ 
            			jTextFieldFechaAlteracionTitular.setText(((Titular)o).getFechaAlteracion());
            		}
            		else{
            			jTextFieldFechaAlteracionTitular.setText("");
            		}
            	}
            	else{
            		jTextFieldFechaAlteracionTitular.setText("");
            	}
                jComboBoxDerecho.setSelectedPatron(((Titular)o).getDerecho().getCodDerecho());
                if (jComboBoxDerecho.getSelectedItem()==null)
                {
                	jComboBoxDerecho.setSelectedIndex(0);                
                }
                jTextFieldPorcentaje.setText(EdicionUtils.getStringValue(((Titular)o).getDerecho().getPorcentajeDerecho()));
                jTextFieldPorcentaje.setText(EdicionUtils.getStringValue(((Titular)o).getDerecho().getPorcentajeDerecho()));
                jTextFieldNifConyuge.setText(((Titular)o).getNifConyuge());
                
                if (((Titular)o).getNifCb() != null && ((Titular)o).getNifCb().length()>0){
                	ComunidadBienes comunidadBienes = loadComunidadBienes(bienInmuebleJuridico, ((Titular)o).getNifCb());
                	jComboBoxComunidadBienes.setSelectedItem(comunidadBienes);
                	if (jComboBoxComunidadBienes.getSelectedItem()==null)
                	{
                		if(jComboBoxComunidadBienes.getItemCount() != 0){
                			jComboBoxComunidadBienes.setSelectedIndex(0);   
                		}
                	}
                	updateUI();
                }
                
            }   
            else if (o instanceof ComunidadBienes){  

            	this.comunidadbienes = (ComunidadBienes)o;
                loadComunidadBienes(bienInmuebleJuridico);
            	jComboBoxComunidadBienes.setSelectedItem((ComunidadBienes)o);
            	if (jComboBoxComunidadBienes.getSelectedItem()==null)
            	{
            		if(jComboBoxComunidadBienes.getItemCount() != 0){
            			jComboBoxComunidadBienes.setSelectedIndex(0);    
            		}
            	}
            	updateUI();
            }

        }         
    }
    
    private EstructuraDB obtenerElementoLista(ArrayList lst, String patron) {
		
    	for (Iterator iteratorLista = lst.iterator();iteratorLista.hasNext();){
    		EstructuraDB estructura = (EstructuraDB)iteratorLista.next();
    		if(estructura.getPatron().equals(patron))
    			return estructura;
    	}
		return null;
	}

	public void loadComunidadBienes(BienInmuebleJuridico bij) 
    {
        if (blackboard.get("ComBienes")!=null && !blackboard.getBoolean("ComBienes")) {

            if(bij.getLstComBienes() != null) {
                Iterator itComBienes = bij.getLstComBienes().iterator();
                
                ComunidadBienes comunidadBienesTemp = new ComunidadBienes("");
                jComboBoxComunidadBienes.addItem(comunidadBienesTemp);                
                jComboBoxComunidadBienes.setSelectedItem(comunidadBienesTemp);
                jComboBoxComunidadBienes.removeAllItems();
                jTextFieldComunidadBienes.setText("");
                while (itComBienes.hasNext()){
//                	jComboBoxComunidadBienes.addItem(new ComunidadBienes(""));
                	jComboBoxComunidadBienes.addItem((ComunidadBienes)itComBienes.next());
                	blackboard.put("ComBienes", true);
                }
            }else{
            	bij.setLstComBienes(new ArrayList());
            }
        }
    }
	
	public ComunidadBienes loadComunidadBienes(BienInmuebleJuridico bij, String nif) 
    {        
        if (blackboard.get("ComBienes")!=null && !blackboard.getBoolean("ComBienes")) {

            if(bij.getLstComBienes() != null){
                Iterator itComBienes = bij.getLstComBienes().iterator();

                while (itComBienes.hasNext())
                {
                    ComunidadBienes comunidad = ((ComunidadBienes)itComBienes.next());
                    if (comunidad.getNif()!= null && comunidad.getNif().equals(nif)){
                        return comunidad;
                    }
                }
            }
        } 
        return null;
    }
    
    public JPanel getJPanelFinca()
    {                  
        if (jPanelFinca  == null)
        {         
        	jPanelFinca = new JPanel(new GridBagLayout());
        	jPanelFinca.setSize(new Dimension(400, 210));
            jPanelFinca.setPreferredSize(new Dimension(400, 210));
            jPanelFinca.setMaximumSize(jPanelFinca.getPreferredSize());
            jPanelFinca.setMinimumSize(jPanelFinca.getPreferredSize());
            
            jPanelFinca.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.panel.datosfinca.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
           
            jPanelFinca.setLayout(new GridBagLayout());
            jPanelFinca.add(getJPanelDatosFinca(), 
            		new GridBagConstraints(0,1,6,1,0.1, 0.2,GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,40));
                    //new GridBagConstraints(0, 0, 6, 2, 0.1, 0.1, 
                    //        GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            //jPanelFinca.add(new JPanel(),
            //        new GridBagConstraints(0, 1, 6, 1, 0.5, 0, GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelFinca.add(getJPanelBotoneraFinca(), 
                    new GridBagConstraints(0, 2, 6, 1, 0.1, 0.1, GridBagConstraints.EAST,
                    		GridBagConstraints.NONE, new Insets (0,5,0,5), 0,0));                         
        }
        return jPanelFinca;
    }
    
    /**
     * This method initializes jPanelDatosFinca 
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelDatosFinca()
    {
        if (jPanelDatosFinca == null)
        {       
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints(6, 5, 4, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints12.gridwidth = 4;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints(6, 2, 5, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints11.gridx = 4;
            gridBagConstraints11.gridwidth = 6;
            gridBagConstraints11.gridy = 2;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints(12, 2, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints10.gridx = 13;
            gridBagConstraints10.gridy = 2;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints(12, 6, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints9.gridx = 13;
            gridBagConstraints9.gridy = 6;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints(12, 3, 1, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints8.gridx = 13;
            gridBagConstraints8.gridwidth = 1;
            gridBagConstraints8.gridy = 3;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints(6, 6, 4, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints5.gridwidth = 7;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints(1, 3, 1, 1, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints4.anchor = GridBagConstraints.WEST;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints(0, 3, 1, 1, 0.1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints3.anchor = GridBagConstraints.WEST;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints(6, 3, 5, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints2.gridx = 4;
            gridBagConstraints2.anchor = GridBagConstraints.WEST;
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridwidth = 7;
            gridBagConstraints2.gridy = 3;
            GridBagConstraints gridBagConstraints = new GridBagConstraints(6, 6, 5, 1, 0.5, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints.ipadx = 0;
            jLabelSupCubierta = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelSupCubierta.setText(I18N.get("Expedientes", "finca.panel.datosfinca.supcubierta")); //$NON-NLS-1$
            
            jLabelSupSobre = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelSupSobre.setText(I18N.get("Expedientes", "finca.panel.datosfinca.supsobre")); //$NON-NLS-1$
            
            jLabelSupTotal = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelSupTotal.setText(I18N.get("Expedientes", "finca.panel.datosfinca.subtotal")); //$NON-NLS-1$
            
            jLabelSupSolar = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelSupSolar.setText(I18N.get("Expedientes", "finca.panel.datosfinca.supsolar")); //$NON-NLS-1$
            
            jLabelParaje = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelParaje.setText(I18N.get("Expedientes", "finca.panel.datosfinca.paraje")); //$NON-NLS-1$
            
            jLabelParcela = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelParcela.setText(I18N.get("Expedientes", "finca.panel.datosfinca.parcela")); //$NON-NLS-1$
            
            jLabelPoligono = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelPoligono.setText(I18N.get("Expedientes", "finca.panel.datosfinca.poligono")); //$NON-NLS-1$
            jLabelAnioPonencia = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelAnioPonencia.setText(I18N.get("Expedientes", "finca.panel.datosfinca.anioponencia")); //$NON-NLS-1$
            jLabelNumero = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelNumero.setText(I18N.get("Expedientes", "finca.panel.datosfinca.numero")); //$NON-NLS-1$
            jLabelVia = new JLabel("", JLabel.CENTER); //$NON-NLS-1$
            jLabelVia.setText(I18N.get("Expedientes", "finca.panel.datosfinca.via")); //$NON-NLS-1$
            
            jLabelTipoCodigoVia = new JLabel("", JLabel.CENTER); 
            jLabelTipoCodigoVia.setText(I18N.get("Expedientes", "finca.panel.datosfinca.codigovia")); 
            
            
            jLabelTipoVia = new JLabel("", JLabel.CENTER); 
            jLabelTipoVia.setText(I18N.get("Expedientes", "finca.panel.datosfinca.tipovia")); 
            
            jLabelRefCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral1.setText(I18N.get("Expedientes", "finca.panel.datosfinca.catastral1")); 
            jLabelRefCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral2.setText(I18N.get("Expedientes", "finca.panel.datosfinca.catastral.2")); 
            
            jLabelFechaAlteracion  = new JLabel("",JLabel.CENTER);
            jLabelFechaAlteracion.setText(I18N.get("Expedientes","finca.panel.datosfinca.fechaalteracion"));
            
            jPanelDatosFinca = new JPanel(new GridBagLayout());
            jPanelDatosFinca.setSize(new Dimension(400, 80));
            jPanelDatosFinca.setPreferredSize(new Dimension(400, 80));
            jPanelDatosFinca.setMaximumSize(jPanelDatosFinca.getPreferredSize());
            jPanelDatosFinca.setMinimumSize(jPanelDatosFinca.getPreferredSize());
            //jPanelDatosFinca.setPreferredSize(new Dimension(400, 50));
            //jPanelDatosFinca.setBorder(BorderFactory.createTitledBorder
            //        (null, I18N.get("Expedientes", "finca.panel.datosfinca.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosFinca.add(jLabelRefCatastral1, 
                    new GridBagConstraints(0,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelRefCatastral2, 
                    new GridBagConstraints(2,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosFinca.add(getJTextFieldRefCatastral1(), 
                    new GridBagConstraints(1,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldRefCatastral2(), 
                    new GridBagConstraints(3,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
           
            jPanelDatosFinca.add(jLabelFechaAlteracion,
            		new GridBagConstraints(0,2,1,1,0.1, 0,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelAnioPonencia, 
                    new GridBagConstraints(0,2,1,1,0.1, 0,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0,5,0,5),0,0));
            
            jPanelDatosFinca.add(jLabelTipoCodigoVia, 
                    new GridBagConstraints(1,2,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosFinca.add(jLabelTipoVia, 
                    new GridBagConstraints(2,2,2,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelVia, gridBagConstraints11);
            jPanelDatosFinca.add(jLabelNumero, gridBagConstraints10);
            jPanelDatosFinca.add(getJTextFieldFechaAlteracion(), gridBagConstraints3);
            jPanelDatosFinca.add(getJComboBoxAnioPonencia(), gridBagConstraints3);
            
            jPanelDatosFinca.add(getJTextFieldCodigoVia(), gridBagConstraints4);
            jPanelDatosFinca.add(getJComboBoxTipoVia(), 
                    new GridBagConstraints(2,3,2,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldVia(), gridBagConstraints2);
            
            jPanelDatosFinca.add(getJButtonBuscarVia(), 
                    new GridBagConstraints(11,3,1,1,0, 0,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldNumero(), gridBagConstraints8);
                    
            jPanelDatosFinca.add(jLabelSupSolar, 
                    new GridBagConstraints(0,5,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelSupTotal, 
                    new GridBagConstraints(1,5,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelSupSobre, 
                    new GridBagConstraints(2,5,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelSupCubierta, 
                    new GridBagConstraints(3,5,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelPoligono, 
                    new GridBagConstraints(4,5,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelParcela, 
                    new GridBagConstraints(5,5,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(jLabelParaje, gridBagConstraints12);
                        
            jPanelDatosFinca.add(getJTextFieldSupSolar(), 
                    new GridBagConstraints(0,6,1,1,0.1, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldSupTotal(), 
                    new GridBagConstraints(1,6,1,1,0.1, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldSupSobre(), 
                    new GridBagConstraints(2,6,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),40,0));
            jPanelDatosFinca.add(getJTextFieldSupCubierta(), 
                    new GridBagConstraints(3,6,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),40,0));
            
            jPanelDatosFinca.add(getJComboBoxPoligono(), 
                    new GridBagConstraints(4,6,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldParcela(), 
                    new GridBagConstraints(5,6,1,1,0.5, 0,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosFinca.add(getJTextFieldParaje(), gridBagConstraints5);
            jPanelDatosFinca.add(getJButtonBuscarParaje(), gridBagConstraints9);
            
                        
            /*jPanelDatosFinca.add(getJPanelBotoneraFinca(), 
                    new GridBagConstraints(4,7,10,1,0.5, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5,5,0,5),0,0));
            */    
            jLabelFechaAlteracion.setVisible(false);
            jTextFieldFechaAlteracion.setVisible(false);
            
            jButtonBuscarParaje.setSize(new Dimension(20,20));
            jTextFieldFechaAlteracion.setMaximumSize(new Dimension(20,20));
        }
        return jPanelDatosFinca;
    }
    
    /**
     * This method initializes jPanelBotoneraFinca  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelBotoneraFinca()
    {
        if (jPanelBotoneraFinca == null)
        {
            jPanelBotoneraFinca = new JPanel(new GridBagLayout());
            //jPanelBotoneraFinca.setPreferredSize(new Dimension(400, 30));
            //jPanelBotoneraFinca.setMaximumSize(jPanelBotoneraFinca.getPreferredSize());
            //jPanelBotoneraFinca.setMinimumSize(jPanelBotoneraFinca.getPreferredSize());
            
            jPanelBotoneraFinca.add(getJButtonMasDatosFinca(), 
                    new GridBagConstraints(5, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraFinca.add(getJButtonNuevaFinca(), 
                    new GridBagConstraints(6, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraFinca.add(getJButtonEliminarFinca(), 
                    new GridBagConstraints(7, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraFinca.add(new JPanel(),
                    new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));        
        }
        return jPanelBotoneraFinca;
    }
    
    /**
     * This method initializes jButtonMasDatosFinca 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosFinca()
    {
        if (jButtonMasDatosFinca == null)
        {
            jButtonMasDatosFinca = new JButton();
            jButtonMasDatosFinca.setText(I18N.get("Expedientes", "finca.boton.datosfinca.masdatos")); 
            jButtonMasDatosFinca.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "Parcela";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    recopilarDatosPanelFinca();
                    FincaExtendedInfoDialog feif =
                        new FincaExtendedInfoDialog(fincaEditada, isEditable, false);
                    feif.setVisible(true); 
                    FincaCatastro fc = feif.getFincaCatastro(fincaEditada);
                    if (fc!=null)
                    {
                        loadData(fincaEditada);
                    }
                }
                    });            
            jButtonMasDatosFinca.setName("_Cmasdatosfinca");
        }
        return jButtonMasDatosFinca;
    }
    
    
    
    /**
     * This method initializes jButtonNuevaFinca    
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevaFinca()
    {
        if (jButtonNuevaFinca == null)
        {
            jButtonNuevaFinca = new JButton();
            jButtonNuevaFinca.setText(I18N.get("Expedientes", "finca.boton.datosfinca.nueva"));
            jButtonNuevaFinca.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "Parcela";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    FincaExtendedInfoDialog feif = new FincaExtendedInfoDialog(isEditable);
                    feif.setVisible(true);   
                    
                    FincaCatastro fc = feif.getFincaCatastro(null);
                    try
                    {
                        if(fc!=null)
                        {
                            Object parcela = ConstantesRegExp.clienteCatastro.existeParcelaEnBD(fc.getRefFinca().getRefCatastral());

                            if (parcela != null)
                            {
                                JOptionPane
                                .showMessageDialog(
                                        FincaPanel.this,
                                        I18N.get("Expedientes", "Error.M14"),
                                        "Error.M14",
                                        JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            HideableNode nodo = panelExpedientes.addObject((DefaultMutableTreeNode)arbol.getModel().getRoot(),fc,true);
                            panelExpedientes.annadeFincaOBI(fc);
                            actualizaArbol(nodo);
                            loadData(fc);
                        }
                    }
                    catch (Exception e1)
                    {
						JOptionPane
                        .showMessageDialog(
                                FincaPanel.this,
                                I18N.get("Expedientes", "Error.M14"),
                                "Error.M14",
                                JOptionPane.ERROR_MESSAGE);
						return;
					}
                }
            });
            jButtonNuevaFinca.setName("_nuevafinca");
        }
        return jButtonNuevaFinca;
    }
    
    /**
     * This method initializes jButtonEliminarFinca 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarFinca()
    {
        if (jButtonEliminarFinca == null)
        {
            jButtonEliminarFinca = new JButton();
            jButtonEliminarFinca.setText(I18N.get("Expedientes", "finca.boton.datosfinca.eliminar"));
            jButtonEliminarFinca.setName("_borrarfinca");

            jButtonEliminarFinca.addActionListener(new java.awt.event.ActionListener()
            {
            	public void actionPerformed(java.awt.event.ActionEvent e)
            	{
                    focoEn = "Parcela";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if (JOptionPane.showConfirmDialog(
            				(Component) FincaPanel.this,
            				I18N.get("Expedientes","fincapanel.removefincaquestion"),
            				I18N.get("Expedientes","fincapanel.removefinca"),
            				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        compruebaEliminarFinca();
                    }
            	}
            });
        }
        return jButtonEliminarFinca;
    }
    
    /**
     * This method initializes jPanelBienInmueble   
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelBienInmueble()
    {
        if (jPanelBienInmueble == null)
        {
            jPanelBienInmueble = new JPanel(new GridBagLayout());
            jPanelBienInmueble.setSize(new Dimension(400, 435));
            jPanelBienInmueble.setPreferredSize(new Dimension(400, 435));
            jPanelBienInmueble.setMaximumSize(jPanelBienInmueble.getPreferredSize());
            jPanelBienInmueble.setMinimumSize(jPanelBienInmueble.getPreferredSize());
            
            jPanelBienInmueble.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.panel.datosinmueble.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            
            jPanelBienInmueble.add(getJPanelBienInmuebleGlobal(), 
                    new GridBagConstraints(0,0,4,1,0.1, 0.2,GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelBienInmueble.add(getJPanelLocal(),             		
                    new GridBagConstraints(0,1,6,1,0.1, 0.2,GridBagConstraints.NORTH, 
                            GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelBienInmueble.add(getJPanelTitular(), 
                    new GridBagConstraints(0,2,6,1,0.1, 0.2,GridBagConstraints.NORTH, 
                            GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
                     
            
        }
        return jPanelBienInmueble;
    }
    
    /**
     * This method initializes jScrollPaneTitular   
     *  
     * @return javax.swing.JScrollPane  
     */
    public JScrollPane getJScrollPaneTitular()
    {
        if (jScrollPaneTitular == null)
        {
            jScrollPaneTitular = new JScrollPane();
            jScrollPaneTitular.setPreferredSize(new Dimension(400, 17));
            jScrollPaneTitular.setMaximumSize(jScrollPaneTitular.getPreferredSize());
            jScrollPaneTitular.setMinimumSize(jScrollPaneTitular.getPreferredSize());
            jScrollPaneTitular.setViewportView(getJTableTitular());
        }
        return jScrollPaneTitular;
    }
    
    public JScrollPane getJScrollPaneFincas()
    {
        if (jScrollPaneFincas  == null)
        {
            jScrollPaneFincas = new JScrollPane();
            jScrollPaneFincas.setPreferredSize(new Dimension(400, 54));
            jScrollPaneFincas.setMaximumSize(jScrollPaneFincas.getPreferredSize());
            jScrollPaneFincas.setMinimumSize(jScrollPaneFincas.getPreferredSize());
            jScrollPaneFincas.setViewportView(getJTableFincas());
        }
        return jScrollPaneFincas;
    }
    
    public JScrollPane getJScrollPaneLocal()
    {
        if (jScrollPaneLocal == null)
        {
            jScrollPaneLocal = new JScrollPane();
            //jScrollPaneLocal.setPreferredSize(new Dimension(400, 100));
            //jScrollPaneLocal.setMaximumSize(jScrollPaneLocal.getPreferredSize());
            //jScrollPaneLocal.setMinimumSize(jScrollPaneLocal.getPreferredSize());
            jScrollPaneLocal.setViewportView(getJTableLocal());
            
        }
        return jScrollPaneLocal;
    }
    
    public JScrollPane getJScrollPaneCultivo()
    {
        if (jScrollPaneCultivo == null)
        {
            jScrollPaneCultivo = new JScrollPane();
            //jScrollPaneCultivo.setPreferredSize(new Dimension(400, 100));
            //jScrollPaneCultivo.setMaximumSize(jScrollPaneCultivo.getPreferredSize());
            //jScrollPaneCultivo.setMinimumSize(jScrollPaneCultivo.getPreferredSize());
            jScrollPaneCultivo.setViewportView(getJTableCultivo());
        }
        return jScrollPaneCultivo;
    }
    
    /**
     * This method initializes jTableTitular    
     *  
     * @return javax.swing.JTable   
     */
    private JTable getJTableTitular()
    {
        if (jTableTitular == null)
        {
            jTableTitular = new JTable();
            
            tablepersonamodel = new TablePersonaModel();
            
            TableSorted tblSorted= new TableSorted(tablepersonamodel);
            tblSorted.setTableHeader(jTableTitular.getTableHeader());
            jTableTitular.setModel(tblSorted);
            jTableTitular.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableTitular.setCellSelectionEnabled(false);
            jTableTitular.setColumnSelectionAllowed(false);
            jTableTitular.setRowSelectionAllowed(true);
            jTableTitular.getTableHeader().setReorderingAllowed(false);
            
        }
        return jTableTitular;
    }
    
    /**
     * This method initializes jTableFincas	
     * 	
     * @return javax.swing.JTable	
     */
    private JTable getJTableFincas()
    {
        if (jTableFincas  == null)
        {
            jTableFincas = new JTable();

            tablefincamodel = new TableFincaCatastroModel();
            
            TableSorted tblSorted= new TableSorted(tablefincamodel);
            tblSorted.setTableHeader(jTableFincas.getTableHeader());
            jTableFincas.setModel(tblSorted);
            jTableFincas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableFincas.setCellSelectionEnabled(false);
            jTableFincas.setColumnSelectionAllowed(false);
            jTableFincas.setRowSelectionAllowed(true);
            jTableFincas.getTableHeader().setReorderingAllowed(false);
            
            ArrayList lst = new ArrayList();
            lst.add(new FincaCatastro());
            ((TableFincaCatastroModel)((TableSorted)jTableFincas.getModel()).
                    getTableModel()).setData(lst);
            
            
            /*
            EdicionOperations oper = new EdicionOperations();
            try
            {
                ArrayList lstVias = oper.obtenerViasCatastro();
                ((TableViasCatastroModel)((TableSorted)jTableViasCatastro.getModel()).getTableModel()).setData(lstVias);
                
            } catch (DataException e1)
            {                
                e1.printStackTrace();
            }
            */
        }
        return jTableFincas;
    }
    
    private JTable getJTableLocal()
    {
        if (jTableLocal == null)
        {
            jTableLocal = new JTable();
            
            tablelocalmodel = new TableLocalModel();
            
            TableSorted tblSorted= new TableSorted(tablelocalmodel);
            tblSorted.setTableHeader(jTableLocal.getTableHeader());
            jTableLocal.setModel(tblSorted);
            jTableLocal.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableLocal.setCellSelectionEnabled(false);
            jTableLocal.setColumnSelectionAllowed(false);
            jTableLocal.setRowSelectionAllowed(true);
            jTableLocal.getTableHeader().setReorderingAllowed(false);
            
        }
        return jTableLocal;
    }
    
    private JTable getJTableCultivo()
    {
        if (jTableCultivo == null)
        {
            jTableCultivo = new JTable();
            
            tablecultivomodel = new TableCultivoModel();
            
            TableSorted tblSorted= new TableSorted(tablecultivomodel);
            tblSorted.setTableHeader(jTableCultivo.getTableHeader());
            jTableCultivo.setModel(tblSorted);
            jTableCultivo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableCultivo.setCellSelectionEnabled(false);
            jTableCultivo.setColumnSelectionAllowed(false);
            jTableCultivo.setRowSelectionAllowed(true);
            jTableCultivo.getTableHeader().setReorderingAllowed(false);
            
        }
        return jTableCultivo;
    }
    
    
    /**
     * This method initializes jPanelRefCatastralFinca 
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelRefCatastralFinca()
    {
        if (jPanelRefCatastralFinca == null)
        {      
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints(3, 0, 1, 1, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.weightx = 0.0;
            jPanelRefCatastralFinca = new JPanel(new GridBagLayout());
            
            jLabelRefCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral1.setText(I18N.get("Expedientes", "finca.panel.datosfinca.catastral1")); 
            jLabelRefCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral2.setText(I18N.get("Expedientes", "finca.panel.datosfinca.catastral.2")); 
            
            jPanelRefCatastralFinca.add(jLabelRefCatastral1, 
                    new GridBagConstraints(0,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralFinca.add(jLabelRefCatastral2, 
                    new GridBagConstraints(2,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelRefCatastralFinca.add(getJTextFieldRefCatastral1(), 
                    new GridBagConstraints(1,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralFinca.add(getJTextFieldRefCatastral2(), gridBagConstraints1);
            
            
        }
        return jPanelRefCatastralFinca;
    }
    
    
    /**
     * This method initializes jTextFieldRefCatastral1   
     *  
     * @return javax.swing.JTextField   
     */    
    private JTextField getJTextFieldRefCatastral1()
    {
        if (jTextFieldRefCatastral1 == null)
        {
            jTextFieldRefCatastral1 = new TextField(7);
            jTextFieldRefCatastral1.setEditable(false);
        }
        return jTextFieldRefCatastral1;
    }
    
    /**
     * This method initializes jTextFieldRefCatastral2   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldRefCatastral2()
    {
        if (jTextFieldRefCatastral2 == null)
        {
            jTextFieldRefCatastral2 = new TextField(7);
            jTextFieldRefCatastral2.setEditable(false);
        }
        return jTextFieldRefCatastral2;
    }
    
    /**
     * This method initializes jComboBoxTipoVia 
     *  
     * @return javax.swing.JComboBox    
     */
    private ComboBoxEstructuras getJComboBoxTipoVia()
    {
        if (jComboBoxTipoVia == null)
        {            
            //Estructuras.cargarEstructura("Tipos de via normalizados del INE");
        	Estructuras.cargarEstructura("Tipo de vía");
            jComboBoxTipoVia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
            
        }
        return jComboBoxTipoVia;
    }
    
    /**
     * This method initializes jTextFieldVia    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldVia()
    {
        if (jTextFieldVia == null)
        {
            jTextFieldVia = new TextField(25);
        }
        return jTextFieldVia;
    }
    
    /**
     * This method initializes jTextFieldCodigoVia    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldCodigoVia()
    {
    	 if (jTextFieldCodigoVia == null)
         {
    		 jTextFieldCodigoVia = new JTextField();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
    		 jTextFieldCodigoVia.addCaretListener(new CaretListener()
                     {
                         public void caretUpdate(CaretEvent evt)
                         {
                             EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCodigoVia,5, app.getMainFrame());
                         }
                     });
         }
         return jTextFieldCodigoVia;
    }
    
    /**
     * This method initializes jTextFieldFechaAlteracion    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldFechaAlteracion()
    {
        if (jTextFieldFechaAlteracion  == null)
        {
        	jTextFieldFechaAlteracion = new TextField(10);        	
        }
        return jTextFieldFechaAlteracion;
    }
    
    private JTextField getJTextFieldFechaAlteracionTitular()
    {
        if (jTextFieldFechaAlteracionTitular  == null)
        {
        	jTextFieldFechaAlteracionTitular = new TextField(10);        	
        }
        return jTextFieldFechaAlteracionTitular;
    }
    
    /**
     * This method initializes jButtonBuscarVia 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonBuscarVia()
    {
        if (jButtonBuscarVia == null)
        {
            jButtonBuscarVia = new JButton();
            jButtonBuscarVia.setIcon(IconLoader.icon(InfCatatralTitularidadPanel.ICONO_BUSCAR));
            
            jButtonBuscarVia.setMaximumSize(new Dimension(20,20));
            jButtonBuscarVia.setSize(new Dimension(20,20));
            
            jButtonBuscarVia.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	focoEn = "Parcela";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                	String tipoVia = "";
                    if(jComboBoxTipoVia.getSelectedPatron()!=null){
                    	tipoVia = jComboBoxTipoVia.getSelectedPatron().toString();
                    }
                	//ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldVia.getText());
                    ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldVia.getText(),tipoVia);
                    dialog.setVisible(true);     
                    
                    String nombreVia = dialog.getVia();
                    jTextFieldCodigoVia.setText( new Integer(dialog.getCodigoVia()).toString());
                    jTextFieldVia.setText(nombreVia);
                    jComboBoxTipoVia.setSelectedPatron(dialog.getTipoVia());
                }
                    });
            
            jButtonBuscarVia.setName("_buscarvia");
            
        }
        return jButtonBuscarVia;
    }
    
    /**
     * This method initializes jTextFieldAnioPonencia   
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxAnioPonencia()
    {
        if (jComboBoxAnioPonencia == null)
        {
        	jComboBoxAnioPonencia = new JComboBox();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
        	//jComboBoxAnioPonencia.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxAnioPonencia;
    }
    
    /**
     * This method initializes jTextFieldNumero 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldNumero()
    {
        if (jTextFieldNumero == null)
        {
            jTextFieldNumero = new JTextField();//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));
            jTextFieldNumero.addCaretListener(new CaretListener()
                    {
                        public void caretUpdate(CaretEvent evt)
                        {
                            EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumero,4, app.getMainFrame());
                        }
                    });
        }
        return jTextFieldNumero;
    }
    
       
    /**
     * This method initializes jTextFieldParcela    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldParcela()
    {
        if (jTextFieldParcela == null)
        {
            jTextFieldParcela = new TextField(5);
        }
        return jTextFieldParcela;
    }
    
    /**
     * This method initializes jTextFieldParaje 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldParaje()
    {
        if (jTextFieldParaje == null)
        {
            jTextFieldParaje = new TextField(30);
            //jTextFieldParaje.setEditable(false);
        }
        return jTextFieldParaje;
    }
    
    /**
     * This method initializes jButtonBuscarParaje  
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonBuscarParaje()
    {
        if (jButtonBuscarParaje == null)
        {
            jButtonBuscarParaje = new JButton();     
            jButtonBuscarParaje.setMaximumSize(new Dimension(20,20));
            jButtonBuscarParaje.setIcon(IconLoader.icon(InfCatatralTitularidadPanel.ICONO_BUSCAR));
            
            jButtonBuscarParaje.setSize(new Dimension(20,20));
            jButtonBuscarParaje.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "Parcela";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    ParajesSistemaDialog dialog = new ParajesSistemaDialog(jTextFieldParaje.getText());
                    dialog.setVisible(true);      
                    
                    //jTextFieldParaje.setText(dialog.getParaje());
                    paraje = dialog.getParaje();
                    if(paraje!=null && paraje.getNombre()!=null)
                    	jTextFieldParaje.setText(paraje.getNombre());
                    
                }
            });
            
            jButtonBuscarParaje.setName("_buscarparaje");
           
        }
        return jButtonBuscarParaje;
    }
    
    /**
     * This method initializes jTextFieldSupSolar   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSupSolar()
    {
        if (jTextFieldSupSolar == null)
        {
            jTextFieldSupSolar = new JTextField(10);//new JNumberTextField(JNumberTextField.REAL, new Integer(99999999), false, 2);
            jTextFieldSupSolar.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupSolar,7, app.getMainFrame());
                }
                    });
        }
        return jTextFieldSupSolar;
    }
    
    /**
     * This method initializes jTextFieldSupTotal   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSupTotal()
    {
        if (jTextFieldSupTotal == null)
        {
            jTextFieldSupTotal = new JTextField(10);
            jTextFieldSupTotal.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupTotal,7, app.getMainFrame());
                }
                    });
        }
        return jTextFieldSupTotal;
    }
    
    /**
     * This method initializes jTextFieldSupSobre   
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSupSobre()
    {
        if (jTextFieldSupSobre == null)
        {
            jTextFieldSupSobre = new JTextField();
            jTextFieldSupSobre.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupSobre,7, app.getMainFrame());
                }
                    });
        }
        return jTextFieldSupSobre;
    }
    
    /**
     * This method initializes jTextFieldSupCubierta    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSupCubierta()
    {
        if (jTextFieldSupCubierta == null)
        {
            jTextFieldSupCubierta = new JTextField();
            jTextFieldSupCubierta.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupCubierta,7, app.getMainFrame());
                }
                    });
        }
        return jTextFieldSupCubierta;
    }
    
    
    /**
     * This method initializes jPanelRefCatastralBien 
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelRefCatastralBien()
    {
        if (jPanelRefCatastralBien == null)
        {      
            jPanelRefCatastralBien = new JPanel(new GridBagLayout());
            
            jLabelPCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral1.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.catastral1")); 
            jLabelPCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral2.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.catastral2")); 
            jLabelCargo = new JLabel("", JLabel.CENTER); 
            jLabelCargo.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.cargo")); 
            jLabelDC1 = new JLabel("", JLabel.CENTER); 
            jLabelDC1.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.dc1")); 
            jLabelDC2 = new JLabel("", JLabel.CENTER); 
            jLabelDC2.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.dc2")); 
            
            jPanelRefCatastralBien.add(jLabelPCatastral1, 
                    new GridBagConstraints(0,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(jLabelPCatastral2, 
                    new GridBagConstraints(2,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(jLabelCargo, 
                    new GridBagConstraints(4,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(jLabelDC1, 
                    new GridBagConstraints(6,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(jLabelDC2, 
                    new GridBagConstraints(8,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelRefCatastralBien.add(getJTextFieldPCatastral1(), 
                    new GridBagConstraints(1,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(getJTextFieldPCatastral2(), 
                    new GridBagConstraints(3,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(getJTextFieldCargo(), 
                    new GridBagConstraints(5,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(getJTextFieldDC1(), 
                    new GridBagConstraints(7,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelRefCatastralBien.add(getJTextFieldDC2(), 
                    new GridBagConstraints(9,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            
        }
        return jPanelRefCatastralBien;
    }
    
    
    /**
     * This method initializes jTextFieldPCatastral1 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldPCatastral1()
    {
        if (jTextFieldPCatastral1 == null)
        {
            jTextFieldPCatastral1 = new TextField(7);
            if(this.tipoExpediente!= null && this.tipoExpediente.getCodigoTipoExpediente()!= null 
            		&& this.tipoExpediente.getCodigoTipoExpediente().equals("901N")){
            	jTextFieldPCatastral1.setEditable(true);
            }
            else{
            	jTextFieldPCatastral1.setEditable(false);
            }
        }
        return jTextFieldPCatastral1;
    }
    
    /**
     * This method initializes jTextFieldPCatastral2 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldPCatastral2()
    {
        if (jTextFieldPCatastral2 == null)
        {
            jTextFieldPCatastral2 = new TextField(7);
            if(this.tipoExpediente!= null && this.tipoExpediente.getCodigoTipoExpediente()!= null 
            		&& this.tipoExpediente.getCodigoTipoExpediente().equals("901N")){
            	jTextFieldPCatastral2.setEditable(true);
            }
            else{
            	jTextFieldPCatastral2.setEditable(false);
            }
        }
        return jTextFieldPCatastral2;
    }
    
    /**
     * This method initializes jTextFieldCargo 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldCargo()
    {
        if (jTextFieldCargo == null)
        {
            jTextFieldCargo = new TextField(4);
        }
        return jTextFieldCargo;
    }
    
    /**
     * This method initializes jTextFieldDC1 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldDC1()
    {
        if (jTextFieldDC1 == null)
        {
            jTextFieldDC1 = new TextField(1);
        }
        return jTextFieldDC1;
    }
    
    /**
     * This method initializes jTextFieldDC2 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldDC2()
    {
        if (jTextFieldDC2 == null)
        {
            jTextFieldDC2 = new TextField(1);
        }
        return jTextFieldDC2;
    }
    
    
    /**
     * This method initializes jTextFieldBloque 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldBloque()
    {
        if (jTextFieldBloque == null)
        {
            jTextFieldBloque = new TextField(4);
        }
        return jTextFieldBloque;
    }
    
    /**
     * This method initializes jTextFieldEscalera   
     *  
     * @return javax.swing.JTextField   
     */
       
    private JComboBox getJComboBoxEscalera()
    {
        if (jComboBoxEscalera  == null)
        {
        	jComboBoxEscalera = new JComboBox();
        	jComboBoxEscalera.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxEscalera.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxEscalera;
    }
    
    /**
     * This method initializes jTextFieldPlanta 
     *  
     * @return javax.swing.JTextField   
     */
        
    private JComboBox getJComboBoxPlanta()
    {
        if (jComboBoxPlanta == null)
        {
        	jComboBoxPlanta = new JComboBox();
        	jComboBoxPlanta.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxPlanta.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxPlanta;
    }
    
    /**
     * This method initializes jTextFieldPuerta 
     *  
     * @return javax.swing.JTextField   
     */
       
    private JComboBox getJComboBoxPuerta()
    {
        if (jComboBoxPuerta == null)
        {
        	jComboBoxPuerta = new JComboBox();
        	jComboBoxPuerta.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxPuerta.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxPuerta;
    }
    
    /**
     * This method initializes jTextFieldNumOrdenDH 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldNumOrdenDH()
    {
        if (jTextFieldNumOrdenDH == null)
        {
            jTextFieldNumOrdenDH = new TextField(4);//new JNumberTextField(JNumberTextField.NUMBER);//, new Integer(9999));

        }
        return jTextFieldNumOrdenDH;
    }
    
    /**
     * This method initializes jTextFieldCoefParticipacion  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldCoefParticipacion()
    {
        if (jTextFieldCoefParticipacion == null)
        {
            jTextFieldCoefParticipacion = new JTextField();
            jTextFieldCoefParticipacion.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldCoefParticipacion,10, app.getMainFrame());
                }
                    });
        }
        return jTextFieldCoefParticipacion;
    }
    
    /**
     * This method initializes jPanelLocal  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelLocal()
    {
        if (jPanelLocal == null)
        {           
            jPanelLocal = new JPanel(new GridBagLayout());
            jPanelLocal.setSize(new Dimension(400,135));
            jPanelLocal.setPreferredSize(new Dimension(400, 135));
            jPanelLocal.setMaximumSize(jPanelLocal.getPreferredSize());
            jPanelLocal.setMinimumSize(jPanelLocal.getPreferredSize());
            
            jPanelLocal.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.panel.local.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
                        
            /*jPanelLocal.add(new JPanel(), 
                    new GridBagConstraints(0, 1, 6, 1, 0.5, 0, GridBagConstraints.NORTH,
                    		GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
             */           
            jPanelLocal.add(getJPanelDatosLocal(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.5, 0, GridBagConstraints.NORTH,
                    		GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelLocal.add(getJPanelBotoneraLocal(), 
                    new GridBagConstraints(0, 2, 6, 1, 0, 0, GridBagConstraints.EAST,
                    		GridBagConstraints.NONE, new Insets (0,5,0,5), 0,0));
            
                       
        }
        return jPanelLocal;
    }
    
    public JPanel getJPanelBienInmuebleGlobal()
    {
        if (jPanelBienInmuebleGlobal  == null)
        {           
        	jPanelBienInmuebleGlobal = new JPanel(new GridBagLayout());
            //jPanelLocal.setPreferredSize(new Dimension(400,130));
        	jPanelBienInmuebleGlobal.setSize(new Dimension(400, 90));
        	jPanelBienInmuebleGlobal.setPreferredSize(new Dimension(400, 90));
        	jPanelBienInmuebleGlobal.setMaximumSize(jPanelBienInmuebleGlobal.getPreferredSize());
        	jPanelBienInmuebleGlobal.setMinimumSize(jPanelBienInmuebleGlobal.getPreferredSize());
            
        	          
        	/*jPanelBienInmuebleGlobal.add(new JPanel(), 
                    new GridBagConstraints(0, 1, 8, 1, 0.5, 0, GridBagConstraints.NORTH,
                    		GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));*/
                        
        	jPanelBienInmuebleGlobal.add(getJPanelDatosBienInmueble(), 
                    new GridBagConstraints(0, 0, 8, 1, 0.5, 0, GridBagConstraints.NORTH,
                    		GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
        	jPanelBienInmuebleGlobal.add(getJPanelBotoneraBienInmueble(), 
                    new GridBagConstraints(0, 2, 8, 1, 0, 0, GridBagConstraints.EAST,
                    		GridBagConstraints.NONE, new Insets (0,5,0,5), 0,0));
            
                       
        }
        return jPanelBienInmuebleGlobal;
    }
    
    /**
     * This method initializes jPanelDatosBienInmueble  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelDatosBienInmueble()
    {
        if (jPanelDatosBienInmueble == null)
        {           
            jPanelDatosBienInmueble = new JPanel(new GridBagLayout());
            jPanelDatosBienInmueble.setSize(new Dimension(400, 65));
            jPanelDatosBienInmueble.setPreferredSize(new Dimension(400, 65));
            jPanelDatosBienInmueble.setMaximumSize(jPanelDatosBienInmueble.getPreferredSize());
            jPanelDatosBienInmueble.setMinimumSize(jPanelDatosBienInmueble.getPreferredSize());
            //jPanelDatosBienInmueble.setPreferredSize(new Dimension(400, 60));
            
            jLabelCoefParticipacion = new JLabel("", JLabel.CENTER); 
            jLabelCoefParticipacion.setText(I18N.get("Expedientes", "finca.panel.datosinmueble.coefpartic")); 
            
            jLabelNumOrdenDH = new JLabel("", JLabel.CENTER); 
            jLabelNumOrdenDH.setText(I18N.get("Expedientes", "finca.panel.datosinmueble.numordenDH")); 
            
            jLabelPuerta = new JLabel("", JLabel.CENTER); 
            jLabelPuerta.setText(I18N.get("Expedientes", "finca.panel.datosinmueble.puerta")); 
            
            jLabelPlanta = new JLabel("", JLabel.CENTER); 
            jLabelPlanta.setText(I18N.get("Expedientes", "finca.panel.datosinmueble.planta")); 
            
            jLabelEscalera = new JLabel("", JLabel.CENTER); 
            jLabelEscalera.setText(I18N.get("Expedientes", "finca.panel.datosinmueble.escalera")); 
            
            jLabelBloque = new JLabel("", JLabel.CENTER); 
            jLabelBloque.setText(I18N.get("Expedientes", "finca.panel.datosinmueble.bloque")); 
            
            
            jLabelPCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral1.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.catastral1")); 
            jLabelPCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelPCatastral2.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.catastral2")); 
            jLabelCargo = new JLabel("", JLabel.CENTER); 
            jLabelCargo.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.cargo")); 
            jLabelDC1 = new JLabel("", JLabel.CENTER); 
            jLabelDC1.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.dc1")); 
            jLabelDC2 = new JLabel("", JLabel.CENTER); 
            jLabelDC2.setText(I18N.get("Expedientes", "finca.panel.bieninmueble.dc2")); 
            
            jPanelDatosBienInmueble.add(jLabelPCatastral1, 
                    new GridBagConstraints(0,0,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelPCatastral2, 
                    new GridBagConstraints(6,0,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelCargo, 
                    new GridBagConstraints(12,0,2,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelDC1, 
                    new GridBagConstraints(20,0,1,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelDC2, 
                    new GridBagConstraints(22,0,1,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(getJTextFieldPCatastral1(), 
                    new GridBagConstraints(3,0,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(getJTextFieldPCatastral2(), 
                    new GridBagConstraints(9,0,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(getJTextFieldCargo(), 
                    new GridBagConstraints(14,0,2,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(getJTextFieldDC1(), 
                    new GridBagConstraints(21,0,1,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(getJTextFieldDC2(), 
                    new GridBagConstraints(23,0,1,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(jLabelBloque, 
                    new GridBagConstraints(0,2,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelEscalera, 
                    new GridBagConstraints(3,2,4,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelPlanta, 
                    new GridBagConstraints(7,2,5,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelPuerta, 
                    new GridBagConstraints(12,2,4,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelNumOrdenDH, 
                    new GridBagConstraints(19,2,2,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(jLabelCoefParticipacion, 
                    new GridBagConstraints(21,2,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(getJTextFieldBloque(), 
                    new GridBagConstraints(0,3,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(getJComboBoxEscalera(), 
                    new GridBagConstraints(3,3,4,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(getJComboBoxPlanta(), 
                    new GridBagConstraints(7,3,5,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(getJComboBoxPuerta(), 
                    new GridBagConstraints(12,3,4,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosBienInmueble.add(getJTextFieldNumOrdenDH(), 
                    new GridBagConstraints(19,3,2,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosBienInmueble.add(getJTextFieldCoefParticipacion(), 
                    new GridBagConstraints(21,3,3,1,0.1, 0.1,GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            EdicionUtils.crearMallaPanel(4, 24, jPanelDatosBienInmueble, 0.1, 0.1, GridBagConstraints.CENTER, 
                    		GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0);
           
        }
        return jPanelDatosBienInmueble;
    }
    
    
    /**
     * This method initializes jPanelDatosTitular
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelDatosTitular()
    {
        if (jPanelDatosTitular == null)
        {           
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints(3, 2, 2, 2, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0);
            gridBagConstraints13.gridx = 3;
            gridBagConstraints13.gridy = 2;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints(1, 0, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints7.gridwidth = 3;
            jPanelDatosTitular = new JPanel(new GridBagLayout());
            jPanelDatosTitular.setSize(new Dimension(400,82));
            jPanelDatosTitular.setPreferredSize(new Dimension(400, 82));
            jPanelDatosTitular.setMaximumSize(jPanelDatosTitular.getPreferredSize());
            jPanelDatosTitular.setMinimumSize(jPanelDatosTitular.getPreferredSize());
            
            jLabelNifConyuge = new JLabel("", JLabel.CENTER); 
            jLabelNifConyuge.setText(I18N.get("Expedientes", "finca.panel.titular.nifconyuge")); 
            
            jLabelPorcentaje = new JLabel("", JLabel.CENTER); 
            jLabelPorcentaje.setText(I18N.get("Expedientes", "finca.panel.titular.porcentaje")); 
            
            jLabelDerecho = new JLabel("", JLabel.CENTER); 
            jLabelDerecho.setText(I18N.get("Expedientes", "finca.panel.titular.derecho")); 
            
            jLabelRazonSocial = new JLabel("", JLabel.CENTER); 
            jLabelRazonSocial.setText(I18N.get("Expedientes", "finca.panel.titular.razonsocial")); 
            jLabelNIF = new JLabel("", JLabel.CENTER); 
            jLabelNIF.setText(I18N.get("Expedientes", "finca.panel.titular.nifcif")); 
            jLabelFechaAlteracionTitular = new JLabel("", JLabel.CENTER); 
            jLabelFechaAlteracionTitular.setText(I18N.get("Expedientes", "finca.panel.titular.fechaalteracion")); 
            
            
            jPanelDatosTitular.add(jLabelNIF, 
                    new GridBagConstraints(0, 0, 2, 1, 0.1, 0, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(jLabelRazonSocial,  
                    new GridBagConstraints(3, 0, 2, 1, 0.1, 0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(jLabelFechaAlteracionTitular,
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJTextFieldNif(), 
                    new GridBagConstraints(0, 1, 2, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJTextFieldRazonSocial(), 
                    new GridBagConstraints(2, 1, 3, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJTextFieldFechaAlteracionTitular(), 
                    new GridBagConstraints(5, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(jLabelDerecho, 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(jLabelPorcentaje, 
                    new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(jLabelNifConyuge, 
                    new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJPanelComunidadBienes(),                     
            		new GridBagConstraints(3, 2, 3, 2, 0.1, 0.1, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJComboBoxDerecho(),            		
                    new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJTextFieldPorcentaje(), 
                    new GridBagConstraints(1, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosTitular.add(getJTextFieldNifConyuge(), 
                    new GridBagConstraints(2, 3, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
        }
        return jPanelDatosTitular;
    }
    
    
    /**
     * This method initializes jPanelLocal  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelTitular()
    {
        if (jPanelTitular == null)
        {   
            jPanelTitular = new JPanel(new GridBagLayout());
            jPanelTitular.setSize(new Dimension(400, 145));
            jPanelTitular.setPreferredSize(new Dimension(400, 145));
            jPanelTitular.setMaximumSize(jPanelTitular.getPreferredSize());
            jPanelTitular.setMinimumSize(jPanelTitular.getPreferredSize());
            
            jPanelTitular.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.panel.titular.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelTitular.add(getJPanelBotoneraTitular(), 
                    new GridBagConstraints(0, 2, 4, 1, 0.1, 0.1, GridBagConstraints.EAST,
                            GridBagConstraints.EAST, new Insets (0,5,0,5), 0,0));
            
            jPanelTitular.add(getJPanelDatosTitular(), 
                    new GridBagConstraints(0, 0, 4, 1, 0.1, 0.1, GridBagConstraints.NORTH,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelLocal.add(new JPanel(), 
                    new GridBagConstraints(0, 1, 6, 1, 0.5, 0, GridBagConstraints.NORTH,
                    		GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
                   
                     
        }
        return jPanelTitular;
    }
    
    /**
     * This method initializes jPanelBotoneraLocal  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelBotoneraLocal()
    {
        if (jPanelBotoneraLocal == null)
        {
            jPanelBotoneraLocal = new JPanel(new GridBagLayout());
            //jPanelBotoneraLocal.setPreferredSize(new Dimension(400, 30));
            //jPanelBotoneraLocal.setMaximumSize(jPanelBotoneraLocal.getPreferredSize());
            //jPanelBotoneraLocal.setMinimumSize(jPanelBotoneraLocal.getPreferredSize());
            
            jPanelBotoneraLocal.add(getJButtonMasDatosLocales(), 
                    new GridBagConstraints(1, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonVistaCultivo(), 
                    new GridBagConstraints(2, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonNuevoLocalComun(), 
                    new GridBagConstraints(3, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonNuevoLocal(), 
                    new GridBagConstraints(4, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(getJButtonEliminarLocal(), 
                    new GridBagConstraints(5, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraLocal.add(new JPanel(),
                    new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
        }
        return jPanelBotoneraLocal;
    }
    
    /**
     * This method initializes jButtonMasDatosLocales 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosLocales()
    {
        if (jButtonMasDatosLocales == null)
        {
            jButtonMasDatosLocales = new JButton();
            jButtonMasDatosLocales.setText(I18N.get("Expedientes", "finca.boton.datoslocal.masdatos"));
            
            jButtonMasDatosLocales.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "Local";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if (blackboard.get("Comun")!=null && blackboard.getBoolean("Comun"))
                    {
                        blackboard.put("finca", fincaEditada);
                        LocalComunExtendedInfoDialog dialog =  new LocalComunExtendedInfoDialog(construccion,isEditable);
                        dialog.setVisible(true);

                        ConstruccionCatastro cons = dialog.getConstruccionCatastro(construccion);
        				if (cons!=null)
                        {
                            RepartoCatastro reparto = dialog.getReparto();
                            if (reparto != null)
                            {
                                construccion.getDatosEconomicos().setCodModalidadReparto(reparto.getTipoReparto() + (int)reparto.getPorcentajeReparto());
                                construccion.setNumOrdenBienInmueble("0000");
                                ArrayList lstConstruccionesReparto = reparto.getLstLocales();
                                if (lstConstruccionesReparto!= null && lstConstruccionesReparto.size()>0){
                                	ConstruccionCatastro construccion = obtenerConstruccion(((ElementoReparto)lstConstruccionesReparto.iterator().next()).getId());
                                	if (construccion !=null)
                                		FincaPanel.this.construccion.getDatosEconomicos().setCodUsoPredominante(construccion.getDatosEconomicos().getCodUsoPredominante());
                                }
                                //reparto.setNumOrdenConsRepartir(construccion.getNumOrdenConstruccion());
                                //reparto.setElemRepartido(construccion);
                                annadeReparto(reparto, construccion.getNumOrdenConstruccion(), null);
                            }
                            loadData(construccion);
                        }                        
                    }
                    else
                    {
                        LocalExtendedInfoDialog dialog = new LocalExtendedInfoDialog(construccion,isEditable);
                        dialog.setVisible(true);

                        if (dialog.getConstruccionCatastro(construccion)!=null){
                        	if (construccion.getRefParcela()!=null && construccion.getRefParcela().getRefCatastral()!=null && construccion.getNumOrdenBienInmueble()!=null){
                        		String refFinca = GeopistaUtil.completarConCeros(construccion.getRefParcela().getRefCatastral(),14);
                        		String numBien = GeopistaUtil.completarConCeros(construccion.getNumOrdenBienInmueble(),4);
                        		BienInmuebleCatastro bien = obtenerBien(refFinca + numBien);
                        	}
                        	loadData(construccion);
        				}

                    }
                }
            });
            jButtonMasDatosLocales.setName("_Cmasdatoslocales");
        }
        return jButtonMasDatosLocales;
    }
    
    /**
     * This method initializes jButtonVistaCultivo 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonVistaCultivo()
    {
        if (jButtonVistaCultivo == null)
        {
            jButtonVistaCultivo = new JButton();
            jButtonVistaCultivo.setText(I18N.get("Expedientes", "finca.boton.datoslocal.vistacultivo"));
            jButtonVistaCultivo.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {                   
                    loadCultivos();
                    EdicionUtils.enablePanel(FincaPanel.this, FincaPanel.this.isEditable());
                    asignaFocoManual();     
                    if(fincaEditada==null){
                    	EdicionUtils.enablePanel(getJPanelFinca(), false);
                    	EdicionUtils.disabledButtons(getJPanelFinca(), "_borrarfinca");
                    }
                }
            });
            jButtonVistaCultivo.setName("vistacultivo");
        }
        return jButtonVistaCultivo;
    }
    
    /**
     * This method initializes jButtonNuevoLocalComun 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoLocalComun()
    {
        if (jButtonNuevoLocalComun == null)
        {
            jButtonNuevoLocalComun = new JButton();
            jButtonNuevoLocalComun.setText(I18N.get("Expedientes", "finca.boton.datoslocal.nuevocomun"));
            
            jButtonNuevoLocalComun.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	if (fincaEditada != null)
                		focoEn = "Parcela";
                	else
                		focoEn = "BI";
                	
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    ConstruccionCatastro cons = new ConstruccionCatastro();
                    
                    if(fincaEditada != null){
                    	if (getJPanelDatosFinca().getParent()!=null)
                    	{
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldRefCatastral1().getText(),getJTextFieldRefCatastral2().getText());
                    		cons.setRefParcela(refCatastral);

                            cons.setCodDelegacionMEH(fincaEditada.getCodDelegacionMEH());
                            cons.setCodMunicipio(fincaEditada.getCodMunicipioDGC());
                        }
                    }
                    else{
                    	if (getJPanelDatosBienInmueble().getParent()!=null){
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldPCatastral1().getText(),getJTextFieldPCatastral2().getText());
                    		cons.setRefParcela(refCatastral);
                    	}
                    }
                    
    				if(getJPanelDatosBienInmueble().getParent()!=null)
    					cons.setNumOrdenBienInmueble(getJTextFieldCargo().getText());

    				
    				blackboard.put("finca", fincaEditada);
    				
    				LocalComunExtendedInfoDialog dialog = new LocalComunExtendedInfoDialog(cons,isEditable);
    				dialog.setVisible(true);   
                    
                    //SE reparte entre distintos cargos
                    cons = dialog.getConstruccionCatastro(cons);
                    
                    if (cons!=null)
                    {
                        RepartoCatastro reparto = dialog.getReparto();
                        if (reparto != null)
                        {
                            cons.getDatosEconomicos().setCodModalidadReparto(reparto.getTipoReparto()+ (int)reparto.getPorcentajeReparto());
                            cons.setNumOrdenBienInmueble("0000");
                            //reparto.setNumOrdenConsRepartir(cons.getNumOrdenConstruccion());
                            //reparto.setElemRepartido(cons);
                            annadeReparto(reparto, null, null);
                        }
                        annadeConstruccion(cons, true);
                        loadData(cons);
                    }
                }
            });
            jButtonNuevoLocalComun.setName("_localcomun");
        }
        return jButtonNuevoLocalComun;
    }
    
    private JButton getJButtonNuevoCultivoComun()
    {
        if (jButtonNuevoCultivoComun == null)
        {
            jButtonNuevoCultivoComun = new JButton();
            jButtonNuevoCultivoComun.setText(I18N.get("Expedientes", "finca.boton.datoslocal.nuevocultivocomun"));
            
            jButtonNuevoCultivoComun.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	if (fincaEditada != null)
                		focoEn = "Parcela";
                	else
                		focoEn = "BI";
                	
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    Cultivo cultivo = new Cultivo();
                    
                    if(fincaEditada != null){
                    	if (getJPanelDatosFinca().getParent()!=null)
                    	{
                    		IdCultivo idCultivo = new IdCultivo();
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldRefCatastral1().getText(),getJTextFieldRefCatastral2().getText());
                    		idCultivo.setParcelaCatastral(refCatastral.getRefCatastral());
                    		cultivo.setIdCultivo(idCultivo);          

                            cultivo.setCodDelegacionMEH(fincaEditada.getCodDelegacionMEH());
                            cultivo.setCodMunicipioDGC(fincaEditada.getCodMunicipioDGC());
                        }
                    }
                    else{
                    	if (getJPanelDatosBienInmueble().getParent()!=null){
                    		IdCultivo idCultivo = new IdCultivo();
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldPCatastral1().getText(),getJTextFieldPCatastral2().getText());
                    		idCultivo.setParcelaCatastral(refCatastral.getRefCatastral());
                    		cultivo.setIdCultivo(idCultivo);
                    	}
                    }
                	          
                	blackboard.put("finca", fincaEditada);
    				
                    CultivoComunExtendedInfoDialog dialog =  new CultivoComunExtendedInfoDialog(cultivo,isEditable);
                    dialog.setVisible(true);          
                    
                    cultivo = dialog.getCultivo(cultivo);
                    
                    if (cultivo != null)
                    {
                        RepartoCatastro reparto = dialog.getReparto();
                        if (reparto != null)
                        {
                    		cultivo.setCodModalidadReparto(reparto.getTipoReparto()+ (int)reparto.getPorcentajeReparto());
                    		//reparto.setCodSubparcelaElementoRepartir(cultivo.getCodSubparcela());
                    		//reparto.setCalifCatastralElementoRepartir(cultivo.getIdCultivo().getCalifCultivo());
                            annadeReparto(reparto, null, null);
                        }
                        annadeCultivo(cultivo, true);
                        loadData(cultivo);
                    }
                }
            });
            jButtonNuevoCultivoComun.setName("_cultivocomun");
        }
        return jButtonNuevoCultivoComun;
    }
    
      
    /**
     * This method initializes jButtonNuevoLocal 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoLocal()
    {
        if (jButtonNuevoLocal == null)
        {
            jButtonNuevoLocal = new JButton();
            jButtonNuevoLocal.setText(I18N.get("Expedientes", "finca.boton.datoslocal.nuevo"));
            
            jButtonNuevoLocal.addActionListener(new java.awt.event.ActionListener()
    		{
    			public void actionPerformed(java.awt.event.ActionEvent e)
    			{
                    focoEn = "BI";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    ConstruccionCatastro cons = new ConstruccionCatastro();
                    
                    if(fincaEditada!=null){
                    	if (getJPanelDatosFinca().getParent()!=null){
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldRefCatastral1().getText(),getJTextFieldRefCatastral2().getText());
                    		cons.setRefParcela(refCatastral);

                            cons.setCodDelegacionMEH(fincaEditada.getCodDelegacionMEH());
                            cons.setCodMunicipio(fincaEditada.getCodMunicipioDGC());
                        }
                    }
                    else{
                    	if (getJPanelDatosBienInmueble().getParent()!=null){
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldPCatastral1().getText(),getJTextFieldPCatastral2().getText());
                    		cons.setRefParcela(refCatastral);
                    	}
                    }

    				if(getJPanelDatosBienInmueble().getParent()!=null)
    					cons.setNumOrdenBienInmueble(getJTextFieldCargo().getText());

    				    				
    				LocalExtendedInfoDialog dialog = new LocalExtendedInfoDialog(cons,isEditable);
    				dialog.setVisible(true);   

    				//Está asociado a un BI    				
    				cons = dialog.getConstruccionCatastro(cons);
    				
    				if (cons!=null)
    				{
    					annadeConstruccion(cons, false);
    					if (cons.getRefParcela()!=null && cons.getRefParcela().getRefCatastral()!=null && cons.getNumOrdenBienInmueble()!=null){
    						String refFinca = GeopistaUtil.completarConCeros(cons.getFinca().getRefFinca().getRefCatastral(),14);
    						String numBien = GeopistaUtil.completarConCeros(cons.getNumOrdenBienInmueble(),4);
    					}
    					loadData(cons);
                    }
    			}
    		});
            jButtonNuevoLocal.setName("_nuevolocal");
        }
        return jButtonNuevoLocal;
    }

	/**
     * This method initializes jButtonEliminarLocal 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarLocal()
    {
        if (jButtonEliminarLocal == null)
        {
            jButtonEliminarLocal = new JButton();
            jButtonEliminarLocal.setText(I18N.get("Expedientes", "finca.boton.datoslocal.eliminar"));
            jButtonEliminarLocal.setName("_borrarlocal");
            
            jButtonEliminarLocal.addActionListener(new java.awt.event.ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
                {
                    focoEn = "Local";
                    componenteConFoco = jTextFieldIdLocal;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if(construccion!=null && construccion.getNumOrdenConstruccion()!=null)
                    {
                        if (JOptionPane.showConfirmDialog(
                                (Component) FincaPanel.this,
                                I18N.get("Expedientes","fincapanel.removelocalquestion"),
                                I18N.get("Expedientes","fincapanel.removelocal"),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            eliminaConstruccion();
                            construccion = null;
                        }
                    }
				}
            	
    		});
            jButtonEliminarLocal.setName("eliminarlocal");
        }
        return jButtonEliminarLocal;
    }
    
    /**
     * This method initializes jPanelBotoneraTitular  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelBotoneraTitular()
    {
        if (jPanelBotoneraTitular == null)
        {
            jPanelBotoneraTitular = new JPanel(new GridBagLayout());
            //jPanelBotoneraTitular.setPreferredSize(new Dimension(400, 30));
            //jPanelBotoneraTitular.setMaximumSize(jPanelBotoneraTitular.getPreferredSize());
            //jPanelBotoneraTitular.setMinimumSize(jPanelBotoneraTitular.getPreferredSize());
                        
            jPanelBotoneraTitular.add(getJButtonMasDatosTitular(), 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraTitular.add(getJButtonComunidadBienes(), 
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraTitular.add(getJButtonNuevoTitular(), 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraTitular.add(getJButtonEliminarTitular(), 
                    new GridBagConstraints(6, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraTitular.add(new JPanel(),
                    new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(5, 0, 5, 0), 0, 0));        
            
        }
        return jPanelBotoneraTitular;
    }
    
    /**
     * This method initializes jButtonMasDatosTitular 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosTitular()
    {
        if (jButtonMasDatosTitular == null)
        {
            jButtonMasDatosTitular = new JButton();
            jButtonMasDatosTitular.setText(I18N.get("Expedientes", "finca.boton.titular.masdatos")); 
            jButtonMasDatosTitular.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "Persona";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    RazonSocialExtendedInfoDialog dialog = new RazonSocialExtendedInfoDialog(
                            titular,RazonSocialExtendedInfoDialog.MODO_TITULAR, isEditable);
                    dialog.setVisible(true);  
                    
                    Titular personaTitular = dialog.getTitular(titular);
                    
                    if (personaTitular != null)
                    {
                        loadData(titular);
                    }
                }
            });
            jButtonMasDatosTitular.setName("_Cmasdatostitular");
        }
        return jButtonMasDatosTitular;
    }
    
    /**
     * This method initializes jButtonNuevoTitular 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoTitular()
    {
        if (jButtonNuevoTitular == null)
        {
            jButtonNuevoTitular = new JButton();
            jButtonNuevoTitular.setText(I18N.get("Expedientes", "finca.boton.titular.nuevo"));
            jButtonNuevoTitular.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "BI";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    RazonSocialExtendedInfoDialog dialog = new RazonSocialExtendedInfoDialog(RazonSocialExtendedInfoDialog.MODO_TITULAR, 
                            isEditable);
                    dialog.setVisible(true);  

                    Titular personaTitular = dialog.getTitular(null);
                    
                    if (personaTitular != null)
                    {
                        if(personaTitular.getDerecho().getCodDerecho() == null || personaTitular.getDerecho().getCodDerecho().equals("")){
                            personaTitular.getDerecho().setCodDerecho("PR");
                            personaTitular.getDerecho().setPorcentajeDerecho(100);
                        }
                        
                        annadeTitular(personaTitular);
                    	loadData(personaTitular);
                    }
                }
            });
            jButtonNuevoTitular.setName("_nuevotitular");
        }
        return jButtonNuevoTitular;
    }
    
    /**
     * This method initializes jButtonEliminarTitular 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarTitular()
    {
        if (jButtonEliminarTitular == null)
        {
            jButtonEliminarTitular = new JButton();
            jButtonEliminarTitular.setText(I18N.get("Expedientes", "finca.boton.titular.eliminar"));
            jButtonEliminarTitular.setName("_borrartitular");
            
            jButtonEliminarTitular.addActionListener(new java.awt.event.ActionListener()
            {
		        public void actionPerformed(java.awt.event.ActionEvent e)
		        {
                    focoEn = "Persona";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if(titular!=null && titular.getNif()!=null)
                    {
                        if (JOptionPane.showConfirmDialog(
                                (Component) FincaPanel.this,
                                I18N.get("Expedientes","fincapanel.removetitularquestion"),
                                I18N.get("Expedientes","fincapanel.removetitular"),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            eliminaTitular();
                            titular = null;
                        }
                    }
                }
            });
        }
        return jButtonEliminarTitular;
    }
    
    /**
     * This method initializes jTextFieldRazonSocial    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldRazonSocial()
    {
        if (jTextFieldRazonSocial == null)
        {
            jTextFieldRazonSocial = new TextField(60);
        }
        return jTextFieldRazonSocial;
    }
    
    /**
     * This method initializes jTextFieldNif    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldNif()
    {
        if (jTextFieldNif == null)
        {
            jTextFieldNif = new TextField(9);
        }
        return jTextFieldNif;
    }
    
    /**
     * This method initializes jPanelComunidadBienes    
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelComunidadBienes()
    {
        if (jPanelComunidadBienes == null)
        {
            jPanelComunidadBienes = new JPanel(new GridBagLayout());
            
            jPanelComunidadBienes.setBorder(BorderFactory.createTitledBorder(I18N.get("Expedientes", "finca.boton.datosCB.titulo"))); 
//          (null, "Comunidad de Bienes", TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
                        
            jPanelComunidadBienes.add(getJComboBoxComunidadBienes(), 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            jPanelComunidadBienes.add(getJTextFieldComunidadBienes(), 
                    new GridBagConstraints(1, 0, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            EdicionUtils.crearMallaPanel(2, 3, jPanelComunidadBienes, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0);
            
        }
        return jPanelComunidadBienes;
    }
    
    /**
     * This method initializes jComboBoxComunidadBienes 
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBoxComunidadBienes()
    {
        if (jComboBoxComunidadBienes == null)
        {
            jComboBoxComunidadBienes = new JComboBox();
            jComboBoxComunidadBienes.setRenderer(new ComunidadBienesCellRenderer());
            //jComboBoxComunidadBienes.setPreferredSize(new Dimension(100, 20));
            
            jComboBoxComunidadBienes.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {   
                    if (jComboBoxComunidadBienes.getSelectedItem() instanceof ComunidadBienes 
                            && (ComunidadBienes)jComboBoxComunidadBienes.getSelectedItem()!=null)
                    {
                        jTextFieldComunidadBienes.setText(((ComunidadBienes)jComboBoxComunidadBienes.
                                getSelectedItem()).getRazonSocial());
                        comunidadbienes = (ComunidadBienes)jComboBoxComunidadBienes.getSelectedItem();
                    }
                        
                }
                    });            
        }
        return jComboBoxComunidadBienes;
    }
    
    /**
     * This method initializes jTextFieldComunidadBienes    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldComunidadBienes()
    {
        if (jTextFieldComunidadBienes == null)
        {
            jTextFieldComunidadBienes = new TextField(60);
            
        }
        return jTextFieldComunidadBienes;
    }
    
    /**
     * This method initializes jButtonNueModComunidadBienes 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNueModComunidadBienes()
    {
        if (jButtonNueModComunidadBienes == null)
        {
            jButtonNueModComunidadBienes = new JButton();
            jButtonNueModComunidadBienes.setText(I18N.get("Expedientes", "finca.boton.datosCB.nuevomodif")); 
        }
        return jButtonNueModComunidadBienes;
    }
    
    /**
     * This method initializes jComboBoxDerecho 
     *  
     * @return javax.swing.JComboBox    
     */
    private ComboBoxEstructuras getJComboBoxDerecho()
    {
        if (jComboBoxDerecho == null)
        {
            Estructuras.cargarEstructura("Tipo de derecho de titularidad");
            jComboBoxDerecho = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxDerecho;
    }
    
    /**
     * This method initializes jTextFieldPorcentaje 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldPorcentaje()
    {
        if (jTextFieldPorcentaje == null)
        {
            jTextFieldPorcentaje = new JTextField();
            jTextFieldPorcentaje.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldPorcentaje,6, app.getMainFrame());
                }
                    });      
        }
        return jTextFieldPorcentaje;
    }
    
    /**
     * This method initializes jTextFieldNifConyuge 
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldNifConyuge()
    {
        if (jTextFieldNifConyuge == null)
        {
            jTextFieldNifConyuge = new TextField(9);
        }
        return jTextFieldNifConyuge;
    }
    
    /**
     * This method initializes jPanelBotoneraBienInmueble  
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelBotoneraBienInmueble()
    {
        if (jPanelBotoneraBienInmueble == null)
        {
            jPanelBotoneraBienInmueble = new JPanel(new GridBagLayout());
            //jPanelBotoneraBienInmueble.setPreferredSize(new Dimension(600, 30));
            //jPanelBotoneraBienInmueble.setMaximumSize(jPanelBotoneraBienInmueble.getPreferredSize());
            //jPanelBotoneraBienInmueble.setMinimumSize(jPanelBotoneraBienInmueble.getPreferredSize());
            
            jPanelBotoneraBienInmueble.add(getJButtonMasDatosInmueble(), 
                    new GridBagConstraints(2, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(getJButtonDefinirRepresentante(), 
                    new GridBagConstraints(1, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(getJButtonNuevoInmueble(), 
                    new GridBagConstraints(3, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(getJButtonEliminarInmueble(), 
                    new GridBagConstraints(4, 0, 1, 1, 0, 0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));
            
            jPanelBotoneraBienInmueble.add(new JPanel(),
                    new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));            
        }
        return jPanelBotoneraBienInmueble;
    }
  
    
    /**
     * This method initializes jButtonMasDatosCultivo 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosCultivo()
    {
        if (jButtonMasDatosCultivo == null)
        {
        	jButtonMasDatosCultivo = new JButton();
        	jButtonMasDatosCultivo.setText(I18N.get("Expedientes", "finca.boton.cultivo.masdatos")); 
        	jButtonMasDatosCultivo.addActionListener(new ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {                    
                    focoEn = "Cultivo";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    recopilarDatosPanelCultivo();
                    actualizaArbol(null);
                    if (blackboard.get("Comun")!=null && blackboard.getBoolean("Comun"))
                    {
                        blackboard.put("finca", fincaEditada);
                        CultivoComunExtendedInfoDialog dialog =  new CultivoComunExtendedInfoDialog(cultivo,isEditable);
                        dialog.setVisible(true);

                        Cultivo cult = dialog.getCultivo(cultivo);
        				if (cult!=null)
                        {
        					loadData(cultivo);
                        }                        
                    }
                    else
                    {
                        CultivoExtendedInfoDialog dialog = new CultivoExtendedInfoDialog(cultivo,isEditable);
                        dialog.setVisible(true);

                        if (dialog.getCultivo(cultivo)!=null){
                        	loadData(cultivo);
        				}
                    }
                }
            });
        	jButtonMasDatosCultivo.setName("_Cmasdatoscultivo");
            
        }
        return jButtonMasDatosCultivo;
    }

    /**
     * This method initializes jButtonMasDatosInmueble 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonMasDatosInmueble()
    {
        if (jButtonMasDatosInmueble == null)
        {
            jButtonMasDatosInmueble = new JButton();
            jButtonMasDatosInmueble.setText(I18N.get("Expedientes", "finca.boton.datosinmueble.masdatos")); 
            jButtonMasDatosInmueble.addActionListener(new ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {

                    focoEn = "BI";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    recopilarDatosPanelBienInmueble();
                    BienInmuebleExtendedInfoDialog dialog =
                        new BienInmuebleExtendedInfoDialog(bienInmuebleJuridico.getBienInmueble(), isEditable);
                    dialog.setVisible(true);                      
                   
                    BienInmuebleJuridico bien = dialog.getBienInmueble(bienInmuebleJuridico);
                    if (bien!=null)
                    {
                        loadData(bienInmuebleJuridico);
                    }
                }
            });
            jButtonMasDatosInmueble.setName("_Cmasdatosinmueble");
            
        }
        return jButtonMasDatosInmueble;
    }
    
    /**
     * This method initializes jButtonDefinirRepresentante 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonDefinirRepresentante()
    {
        if (jButtonDefinirRepresentante == null)
        {
            jButtonDefinirRepresentante = new JButton();
            jButtonDefinirRepresentante.setText(I18N.get("Expedientes", "finca.boton.datosinmueble.definirrepresentante")); 
            jButtonDefinirRepresentante.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "BI";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if(bienInmuebleJuridico!=null && bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getIdBienInmueble()!=null)
                    {
                        RazonSocialExtendedInfoDialog dialog =
                            new RazonSocialExtendedInfoDialog(RazonSocialExtendedInfoDialog.MODO_REPRESENTANTE,
                                    isEditable);
                        if (bienInmuebleJuridico!=null && bienInmuebleJuridico.getBienInmueble()!=null &&
                                bienInmuebleJuridico.getBienInmueble().getRepresentante()!=null
                                && bienInmuebleJuridico.getBienInmueble().getRepresentante().getNif()!=null)
                            dialog.getRazonSocialExtendedInfoPanel().loadData(bienInmuebleJuridico.getBienInmueble().getRepresentante());

                        dialog.setVisible(true);
                        Titular representante = dialog.getTitular(null);
                        if (representante != null)
                        {
                            annadeOActualizaRepresentante(representante);
                        }
                    }
                }
            });
            jButtonDefinirRepresentante.setName("_definirrepresentante");
        }
        return jButtonDefinirRepresentante;
    }
    
    /**
     * This method initializes jButtonNuevoInmueble 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonNuevoInmueble()
    {
        if (jButtonNuevoInmueble == null)
        {
            jButtonNuevoInmueble = new JButton();
            jButtonNuevoInmueble.setText(I18N.get("Expedientes", "finca.boton.datosinmueble.nuevo"));
            jButtonNuevoInmueble.setName("_nuevobien");
           
            jButtonNuevoInmueble.addActionListener(new ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                	if(fincaEditada!=null)
                		focoEn = "Parcela";
                	else
                		focoEn = "BI";
                	
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    BienInmuebleCatastro bic = new BienInmuebleCatastro();
                	if (getJPanelDatosFinca().getParent()!=null)
                    {
    					ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldRefCatastral1().getText(),getJTextFieldRefCatastral2().getText());
    					IdBienInmueble id_bic = new IdBienInmueble();
    					id_bic.setParcelaCatastral(refCatastral.getRefCatastral());
    					bic.setIdBienInmueble(id_bic);

                        //JAVIER
                        if(fincaEditada!= null){
                            bic.setDomicilioTributario(fincaEditada.getDirParcela());
                            bic.setCodMunicipioDGC(fincaEditada.getCodMunicipioDGC());
                            bic.setTipoMovimiento(BienInmuebleCatastro.TIPO_MOVIMIENTO_FINAL);
                        }
                    }
                	
                    //BienInmuebleExtendedInfoDialog dialog = new BienInmuebleExtendedInfoDialog(bic,isEditable);
                	BienInmuebleExtendedInfoDialog dialog = new BienInmuebleExtendedInfoDialog(bic,isEditable, FincaPanel.this.tipoExpediente);
                    dialog.setVisible(true);   
                    
                    BienInmuebleJuridico bien = new BienInmuebleJuridico();
                    bien.setBienInmueble(bic);
                    bien = dialog.getBienInmueble(bien);
                    if (bien!=null)
                    {
                        if(fincaEditada==null)
                        {
                            try
                            {
                                Object bi = ConstantesRegExp.clienteCatastro.existeBIEnBD(bien.getBienInmueble().
                                        getIdBienInmueble().getIdBienInmueble());
                                if (bi != null)
                                {
                                    JOptionPane.showMessageDialog(FincaPanel.this,I18N.get("Expedientes", "Error.J2"),
                                            "Error.J2",JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            }
                            catch (Exception e1)
                            {
                                JOptionPane.showMessageDialog(FincaPanel.this,I18N.get("Expedientes", "Error.J2"),
                                        "Error.J2",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                        annadeBI(bien);
                        loadData(bien);
                    }
                }
           });
   
        }
        return jButtonNuevoInmueble;
    }
    
    /**
     * This method initializes jButtonEliminarInmueble 
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonEliminarInmueble()
    {
        if (jButtonEliminarInmueble == null)
        {
            jButtonEliminarInmueble = new JButton();
            jButtonEliminarInmueble.setText(I18N.get("Expedientes", "finca.boton.datosinmueble.eliminar"));
            jButtonEliminarInmueble.setName("_borrarbien");
            
            jButtonEliminarInmueble.addActionListener(new ActionListener()
            {
		        public void actionPerformed(java.awt.event.ActionEvent e)
		        {
                    focoEn = "BI";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if(bienInmuebleJuridico!=null&&bienInmuebleJuridico.getBienInmueble().
                            getIdBienInmueble().getIdBienInmueble()!=null)
                    {
                        if (JOptionPane.showConfirmDialog(
                                (Component) FincaPanel.this,
                                I18N.get("Expedientes","fincapanel.removebieninmueblequestion"),
                                I18N.get("Expedientes","fincapanel.removebieninmueble"),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            compruebaEliminarBI();
                            bienInmuebleJuridico = null;
                        }
                    }
		        }
            });
        }
        return jButtonEliminarInmueble;
    }
    
    /**
     * This method initializes jPanelBuscarVia 
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelBuscarVia()
    {
        if (jPanelBuscarVia == null)
        {       
            jPanelBuscarVia = new JPanel();
            jPanelBuscarVia.setLayout(new FlowLayout());
            jPanelBuscarVia.add(getJButtonBuscarVia(), FlowLayout.LEFT);
        }
        return jPanelBuscarVia;
    }
    
    /**
     * This method initializes jPanelDatosLocal 
     *  
     * @return javax.swing.JPanel   
     */
    public JPanel getJPanelDatosLocal()
    {
        if (jPanelDatosLocal == null)
        {   
            jLabelCodTipoValor = new JLabel("", JLabel.CENTER);
            jLabelCodTipoValor.setText(I18N.get("Expedientes", "finca.panel.datoslocal.codtipovalor"));
            jLabelCategoria = new JLabel("", JLabel.CENTER);
            jLabelCategoria.setText(I18N.get("Expedientes", "finca.panel.datoslocal.categoria"));
            jLabelTipologia = new JLabel("", JLabel.CENTER);
            jLabelTipologia.setText(I18N.get("Expedientes", "finca.panel.datoslocal.tipologia"));
            jLabelSupTerrazaLocal = new JLabel("", JLabel.CENTER);
            jLabelSupTerrazaLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.supterraza"));
            jLabelSupTotalLocal = new JLabel("", JLabel.CENTER);
            jLabelSupTotalLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.suptotal"));
            jLabelDestinoLocal = new JLabel("", JLabel.CENTER);
            jLabelDestinoLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.destino"));
            jLabelPuertaLocal = new JLabel("", JLabel.CENTER);
            jLabelPuertaLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.puerta"));
            jLabelPlantaLocal = new JLabel("", JLabel.CENTER);
            jLabelPlantaLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.planta"));
            jLabelEscaleraLocal = new JLabel("", JLabel.CENTER);
            jLabelEscaleraLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.escalera"));
            jLabelBloqueLocal = new JLabel("", JLabel.CENTER);
            jLabelBloqueLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.bloque"));
            jLabelUnidadConst = new JLabel("", JLabel.CENTER);
            jLabelUnidadConst.setText(I18N.get("Expedientes", "finca.panel.datoslocal.uc"));
            jLabelIdLocal = new JLabel("", JLabel.CENTER);
            jLabelIdLocal.setText(I18N.get("Expedientes", "finca.panel.datoslocal.id"));
            
            jPanelDatosLocal = new JPanel(new GridBagLayout());
            jPanelDatosLocal.setSize(new Dimension(400, 78));
            jPanelDatosLocal.setPreferredSize(new Dimension(400, 78));
            jPanelDatosLocal.setMaximumSize(jPanelDatosLocal.getPreferredSize());
            jPanelDatosLocal.setMinimumSize(jPanelDatosLocal.getPreferredSize());
            
            jPanelDatosLocal.add(jLabelIdLocal, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelUnidadConst, 
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelBloqueLocal, 
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelEscaleraLocal, 
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelPlantaLocal,
                    new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelPuertaLocal, 
                    new GridBagConstraints(5, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelDestinoLocal, 
                    new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJTextFieldIdLocal(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJTextFieldUnidadConstrLocal(),
                    new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJTextFieldBloqueLocal(), 
                    new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
           
            jPanelDatosLocal.add(getJComboBoxEscaleraLocal(), 
                    new GridBagConstraints(3, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosLocal.add(getJComboBoxPlantaLocal(), 
                    new GridBagConstraints(4, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosLocal.add(getJComboBoxPuertaLocal(), 
                    new GridBagConstraints(5, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJComboBoxDestinoLocal(), 
                    new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelSupTotalLocal, 
                    new GridBagConstraints(6, 0, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelSupTerrazaLocal, 
                    new GridBagConstraints(8, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelTipologia, 
                    new GridBagConstraints(2, 2, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(jLabelCategoria, 
                    new GridBagConstraints(5, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));           
            jPanelDatosLocal.add(jLabelCodTipoValor, 
                    new GridBagConstraints(6, 2, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJTextFieldSupTotalLocal(),
                    new GridBagConstraints(6, 1, 2, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJTextFieldSupTerrazaLocal(), 
                    new GridBagConstraints(8, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));          
            jPanelDatosLocal.add(getJComboBoxTipologiaLocal(), 
                    new GridBagConstraints(2, 3, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosLocal.add(getJComboBoxCategoria(), 
                    new GridBagConstraints(5, 3, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));          
            jPanelDatosLocal.add(getJComboBoxCodTipoValor(), 
                    new GridBagConstraints(6, 3, 3, 1, 0.1, 0.1, GridBagConstraints.CENTER,
                            GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            initComboBoxes();
            
        }
        return jPanelDatosLocal;
    }
    
    /**
     * This method initializes jTextFieldIdLocal    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldIdLocal()
    {
        if (jTextFieldIdLocal == null)
        {
            jTextFieldIdLocal = new TextField(4);
        }
        return jTextFieldIdLocal;
    }
    
    /**
     * This method initializes jTextFieldUnidadConstrLocal  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldUnidadConstrLocal()
    {
        if (jTextFieldUnidadConstrLocal == null)
        {
            jTextFieldUnidadConstrLocal = new TextField(4);
        }
        return jTextFieldUnidadConstrLocal;
    }
    
    /**
     * This method initializes jTextFieldBloqueLocal    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldBloqueLocal()
    {
        if (jTextFieldBloqueLocal == null)
        {
            jTextFieldBloqueLocal = new TextField(4);
        }
        return jTextFieldBloqueLocal;
    }
    
    /**
     * This method initializes jTextFieldEscaleraLocal  
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxEscaleraLocal()
    {
        if (jComboBoxEscaleraLocal  == null)
        {
        	jComboBoxEscaleraLocal = new JComboBox();
        	jComboBoxEscaleraLocal.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxEscaleraLocal.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxEscaleraLocal;
    }
    
    /**
     * This method initializes jTextFieldPlantaLocal    
     *  
     * @return javax.swing.JTextField   
     */
   
    private JComboBox getJComboBoxPlantaLocal()
    {
        if (jComboBoxPlantaLocal  == null)
        {
        	jComboBoxPlantaLocal = new JComboBox();
        	jComboBoxPlantaLocal.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxPlantaLocal.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxPlantaLocal;
    }
    
    /**
     * This method initializes jTextFieldPuertaLocal    
     *  
     * @return javax.swing.JTextField   
     */
    
    private JComboBox getJComboBoxPuertaLocal()
    {
        if (jComboBoxPuertaLocal == null)
        {
        	jComboBoxPuertaLocal = new JComboBox();
        	jComboBoxPuertaLocal.setRenderer(new EstructuraDBListCellRenderer());
        	jComboBoxPuertaLocal.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxPuertaLocal;
    }
    
    /**
     * This method initializes jComboBoxDestinoLocal    
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBoxDestinoLocal()
    {
        if (jComboBoxDestinoLocal == null)
        {
            jComboBoxDestinoLocal = new JComboBox();    
            jComboBoxDestinoLocal.setRenderer(new EstructuraDBListCellRenderer());
            jComboBoxDestinoLocal.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxDestinoLocal;
    }
    
    /**
     * This method initializes jTextFieldSupTotalLocal  
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSupTotalLocal()
    {
        if (jTextFieldSupTotalLocal == null)
        {
            jTextFieldSupTotalLocal = new JTextField();
            jTextFieldSupTotalLocal.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupTotalLocal,7, app.getMainFrame());
                }
                    });
        }
        return jTextFieldSupTotalLocal;
    }
    
    /**
     * This method initializes jTextFieldSupTerrazaLocal    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldSupTerrazaLocal()
    {
        if (jTextFieldSupTerrazaLocal == null)
        {
            jTextFieldSupTerrazaLocal = new JTextField();
            jTextFieldSupTerrazaLocal.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSupTerrazaLocal,7, app.getMainFrame());
                }
                    });
        }
        return jTextFieldSupTerrazaLocal;
    }
    
    /**
     * This method initializes jComboBoxTipologiaLocal  
     *  
     * @return javax.swing.JComboBox    
     */
    private JComboBox getJComboBoxTipologiaLocal()
    {
        if (jComboBoxTipologiaLocal == null)
        {
            jComboBoxTipologiaLocal = new JComboBox();     
            jComboBoxTipologiaLocal.setRenderer(new EstructuraDBListCellRenderer());
            jComboBoxTipologiaLocal.setKeySelectionManager(new ComboBoxKeyTipoDestinoSelectionManager());
        }
        return jComboBoxTipologiaLocal;
    }
    
    private JComboBox getJComboBoxPoligono()
    {
        if (jComboBoxPoligono == null)
        {
        	jComboBoxPoligono = new JComboBox();     
        }
        return jComboBoxPoligono;
    }
    
    /**
     * This method initializes jComboBoxCategoria   
     *  
     * @return javax.swing.JComboBox    
     */
    private ComboBoxEstructuras getJComboBoxCategoria()
    {
        if (jComboBoxCategoria == null)
        {
            Estructuras.cargarEstructura("Categoría de construcción");
            jComboBoxCategoria = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxCategoria;
    }
    
    /**
     * This method initializes jComboBoxCodTipoValor    
     *  
     * @return javax.swing.JComboBox    
     */
    private ComboBoxEstructuras getJComboBoxCodTipoValor()
    {
        if (jComboBoxCodTipoValor == null)
        {
            Estructuras.cargarEstructura("Codigo Tipo Valor");
            jComboBoxCodTipoValor= new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxCodTipoValor;
    }
    
    
    /**
     * This method initializes jButtonComunidadBienes   
     *  
     * @return javax.swing.JButton  
     */
    private JButton getJButtonComunidadBienes()
    {
        if (jButtonComunidadBienes == null)
        {
            jButtonComunidadBienes = new JButton();
            jButtonComunidadBienes.setText(I18N.get("Expedientes", "finca.boton.titular.cb"));
            jButtonComunidadBienes.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    RazonSocialExtendedInfoDialog dialog = 
                        new RazonSocialExtendedInfoDialog(RazonSocialExtendedInfoDialog.MODO_COMUNIDAD_BIENES, 
                                isEditable);
                    
                    if (comunidadbienes!=null)
                    {
                        dialog.getRazonSocialExtendedInfoPanel().loadData(comunidadbienes);
                    }
                    
                    dialog.setVisible(true);    
                    
                    Titular cb = dialog.getTitular(null);
                    
                    if (cb != null)
                    {
                        annadirComunidadBienes(cb);
                    }
                }
            });
            jButtonComunidadBienes.setName("_combienes");
        }
        return jButtonComunidadBienes;
    }
    
    private JTextField getJTextFieldSubparcela() {
        if (jTextFieldSubparcela == null) {
            jTextFieldSubparcela = new JTextField(5);
        }
        return jTextFieldSubparcela;
    }
    
    /**
     * This method initializes jComboBoxTipoCultivo	
     * 	
     * @return javax.swing.JTextField	
     */
    private JComboBox getJComboBoxTipoCultivo() {
        if (jComboBoxTipoCultivo == null) {
            jComboBoxTipoCultivo = new JComboBox();
            jComboBoxTipoCultivo.setPreferredSize(new Dimension(150, 20));
            jComboBoxTipoCultivo.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxTipoCultivo;
    }
    
    /**
     * This method initializes jComboBoxTipoSubparcela	
     * 	
     * @return javax.swing.JComboBox	
     */
    private ComboBoxEstructuras getJComboBoxTipoSubparcela() {
        if (jComboBoxTipoSubparcela == null) {		
            
            Estructuras.cargarEstructura("Tipo de subparcela");
            jComboBoxTipoSubparcela = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
            
            jComboBoxTipoSubparcela.setPreferredSize(new Dimension(150, 20));
        }
        return jComboBoxTipoSubparcela;
    }
    
    /**
     * This method initializes jComboBoxIntensidadProductiva	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxIntensidadProductiva() {
        if (jComboBoxIntensidadProductiva == null) {
            jComboBoxIntensidadProductiva = new JComboBox();
            jComboBoxIntensidadProductiva.setPreferredSize(new Dimension(150, 20));
            jComboBoxIntensidadProductiva.removeAllItems();
            
            //escribe de 00 a 99 
            int i=0;
            NumberFormat formatter = new DecimalFormat("00");
            jComboBoxIntensidadProductiva.addItem("");
            while (i<100)            
                jComboBoxIntensidadProductiva.addItem(formatter.format(new Integer(i++)));
           
        }
        return jComboBoxIntensidadProductiva;
    }
    
    private JComboBox getJComboBoxClase() {
        if (jComboBoxClase  == null) {
        	Estructuras.cargarEstructura("Clase Cultivo");
        	jComboBoxClase = new ComboBoxEstructuras(Estructuras.getListaTipos(),
    				null, AppContext.getApplicationContext().getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES"), true);
    		    		
            
        }
        return jComboBoxClase;
    }
    
    /**
     * This method initializes jTextFieldSuperficieCultivo
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldSuperficieCultivo() {
        if (jTextFieldSuperficieCultivo == null) {
            jTextFieldSuperficieCultivo = new TextField(10);
            jTextFieldSuperficieCultivo.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSuperficieCultivo,10,app.getMainFrame());
                }
                    });
        }
        return jTextFieldSuperficieCultivo;
    }
    
    /**
     * This method initializes jButtonReparto	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonReparto() {
        if (jButtonReparto == null) {
            jButtonReparto = new JButton();
            jButtonReparto.setText(I18N.get("Expedientes","finca.boton.cultivo.reparto"));
            
            jButtonReparto.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    if (blackboard.get("Comun")!=null
                            && blackboard.getBoolean("Comun"))
                    {
                        blackboard.put("finca", fincaEditada);
                        RepartoExtendedInfoDialog dialog =
                            new RepartoExtendedInfoDialog(cultivo, isEditable);
                        dialog.setVisible(true);
                        //RepartoCatastro reparto = dialog.getReparto();
                        RepartoCatastro reparto = dialog.getReparto(cultivo);
                        if (reparto != null)
                        {
                            cultivo.setCodModalidadReparto(reparto.getTipoReparto() + (int)reparto.getPorcentajeReparto());
                            //reparto.setCodSubparcelaElementoRepartir(cultivo.getCodSubparcela());
                            //reparto.setCalifCatastralElementoRepartir(cultivo.getIdCultivo().getCalifCultivo());
                            annadeReparto(reparto, null, cultivo.getCodSubparcela());
                        }
                        loadData(cultivo);
                    }
                    else
                    {
                         muestraAviso("Error.J1");
                    }
                }
            });
            jButtonReparto.setName("_Creparto");
        }
        return jButtonReparto;
    }
    
    /**
     * This method initializes jButtonVistaLocal	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonVistaLocal() {
        if (jButtonVistaLocal == null) {
            jButtonVistaLocal = new JButton();
            jButtonVistaLocal.setText(I18N.get("Expedientes","finca.boton.cultivo.vistalocal"));
            
            jButtonVistaLocal.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    loadLocales();
                    EdicionUtils.enablePanel(FincaPanel.this, FincaPanel.this.isEditable());
                    asignaFocoManual();
                    if(fincaEditada==null){
                    	EdicionUtils.enablePanel(getJPanelFinca(), false);
                    	EdicionUtils.disabledButtons(getJPanelFinca(), "_borrarfinca");
                    }
                }
                    });
            jButtonVistaLocal.setName("vistalocal");
        }
        return jButtonVistaLocal;
    }
    
    private void loadLocales() {

    	jPanelBienInmueble.remove(getJPanelCultivo());
    	jPanelBienInmueble.add(getJPanelLocal(),
    			new GridBagConstraints(0,1,6,1,0.5, 0.5,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
    	ArrayList lstConst = new ArrayList();

    	Object lastObject = AppContext.getApplicationContext().getBlackboard().get("lastSelected");

    	if(lastObject instanceof BienInmuebleJuridico){
    		
    		if(bienInmuebleJuridico.getBienInmueble().getLstConstrucciones()!=null){
    			if(!bienInmuebleJuridico.getBienInmueble().getLstConstrucciones().isEmpty()){
    				lstConst.addAll(bienInmuebleJuridico.getBienInmueble().getLstConstrucciones());  
    			}
    		
    	}
    	}else if(lastObject instanceof FincaCatastro){

    		lstConst = this.fincaEditada.getLstConstrucciones();
    	}
    	else if(lastObject instanceof ConstruccionCatastro){
    		lstConst.add(this.construccion);
    	}
    	else if(lastObject instanceof Titular){
    		if(titular.getBienInmueble()!=null){
    			if(titular.getBienInmueble().getLstConstrucciones()!=null){
    				if(!titular.getBienInmueble().getLstConstrucciones().isEmpty()){
        				lstConst.addAll(titular.getBienInmueble().getLstConstrucciones());  
        			}
    			}
    		}
    	}

    	if(lstConst!=null){
    		if (lstConst.size()>1)
    		{
    			this.getJPanelLocal().remove(this.getJPanelDatosLocal());    			 			
    			this.getJPanelLocal().add(this.getJScrollPaneLocal(), 
    					new GridBagConstraints(0,0,6,1,0.1, 0.1,GridBagConstraints.NORTH, 
    							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,50));

    			this.loadTableLocal(lstConst); 
    			this.updateUI();
    		}
    		else if(!lstConst.isEmpty())
    		{
    			this.getJPanelLocal().remove(this.getJScrollPaneLocal()); 
    			this.getJPanelLocal().add(this.getJPanelDatosLocal(), 
    					new GridBagConstraints(0,0,6,1,0.1, 0.1,GridBagConstraints.NORTH, 
    							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
    			this.loadData((ConstruccionCatastro)lstConst.iterator().next());  
    			this.updateUI();
    		}  
    		else{
    			this.getJPanelLocal().remove(this.getJScrollPaneLocal()); 
    			this.getJPanelLocal().add(this.getJPanelDatosLocal(), 
    					new GridBagConstraints(0,0,6,1,0.1, 0.1,GridBagConstraints.NORTH, 
    							GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
    			ConstruccionCatastro emptyConstruccion = new ConstruccionCatastro();
    			this.loadData(emptyConstruccion);       
    			this.updateUI();
    		}
    	}
    	else{
    		this.getJPanelLocal().remove(this.getJScrollPaneLocal()); 
    		this.getJPanelLocal().add(this.getJPanelDatosLocal(), 
    				new GridBagConstraints(0,0,6,1,0.1, 0.1,GridBagConstraints.NORTH, 
    						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
    		ConstruccionCatastro emptyConstruccion = new ConstruccionCatastro();
    		this.loadData(emptyConstruccion);  
    		this.updateUI();
    	}
    }
       
    private void loadCultivos() {
        
        jPanelBienInmueble.remove(getJPanelLocal());
        jPanelBienInmueble.add(getJPanelCultivo(),
                new GridBagConstraints(0,1,6,1,0.5, 0.5,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
        
        ArrayList lstCult = new ArrayList();
        Object lastObject = AppContext.getApplicationContext().getBlackboard().get("lastSelected");
        if(lastObject instanceof BienInmuebleJuridico){
            
            if(bienInmuebleJuridico.getBienInmueble().getLstCultivos()!=null){
                
                lstCult.addAll(bienInmuebleJuridico.getBienInmueble().getLstCultivos());   
            }
           
        }else if(lastObject instanceof FincaCatastro){
            
            lstCult = this.fincaEditada.getLstCultivos();
        }
        else if(lastObject instanceof Cultivo){
            lstCult.add(this.cultivo);
        }
        else if(lastObject instanceof Titular){
    		if(titular.getBienInmueble()!=null){
    			if(titular.getBienInmueble().getLstCultivos()!=null){
    				if(!titular.getBienInmueble().getLstCultivos().isEmpty()){
        				lstCult.addAll(titular.getBienInmueble().getLstCultivos());  
        			}
    			}
    		}
    	}
        
        if(lstCult!=null){
        	if (lstCult.size()>1)
        	{        	
        		this.getJPanelCultivo().remove(this.getJPanelDatosCultivo());
        		this.getJPanelCultivo().add(this.getJScrollPaneCultivo(), 
        				new GridBagConstraints(0,0,6,1,0.5, 0.5,GridBagConstraints.NORTH, 
        						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,50));

        		this.loadTableCultivo(lstCult);
        		this.updateUI();
        	}
        	else if(!lstCult.isEmpty())
        	{

        		this.getJPanelCultivo().remove(this.getJScrollPaneCultivo());
        		this.getJPanelCultivo().add(this.getJPanelDatosCultivo(), 
        				new GridBagConstraints(0,0,6,1,0.5, 0.5,GridBagConstraints.NORTH, 
        						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
        		this.loadData((Cultivo)lstCult.iterator().next());
        		this.updateUI();
        	}  
        	else{

        		this.getJPanelCultivo().remove(this.getJScrollPaneCultivo());
        		this.getJPanelCultivo().add(this.getJPanelDatosCultivo(), 
        				new GridBagConstraints(0,0,6,1,0.5, 0.5,GridBagConstraints.NORTH, 
        						GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
        		Cultivo emptyCultivo = new Cultivo();
        		this.loadData(emptyCultivo);   
        		this.updateUI();
        	}
        }
        else{
        	this.getJPanelCultivo().remove(this.getJScrollPaneCultivo());
        	this.getJPanelCultivo().add(this.getJPanelDatosCultivo(), 
        			new GridBagConstraints(0,0,6,1,0.5, 0.5,GridBagConstraints.NORTH, 
        					GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
        	Cultivo emptyCultivo = new Cultivo();
        	this.loadData(emptyCultivo);    
        	this.updateUI();
        }

    }
      
    /**
     * This method initializes jButtonNuevoCultivo	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonNuevoCultivo()
    {
        if (jButtonNuevoCultivo == null)
        {
            jButtonNuevoCultivo = new JButton();
            jButtonNuevoCultivo.setText(I18N.get("Expedientes","finca.boton.cultivo.nuevocultivo"));
            jButtonNuevoCultivo.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    focoEn = "BI";
                    componenteConFoco = null;
                    focoAutomatico = true;
                    actualizaArbol(null);
                    Cultivo cult = new Cultivo();
                    if(fincaEditada != null){
                    	if (getJPanelDatosFinca().getParent()!=null)
                    	{
                    		IdCultivo idCultivo = new IdCultivo();
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldRefCatastral1().getText(),getJTextFieldRefCatastral2().getText());
                    		idCultivo.setParcelaCatastral(refCatastral.getRefCatastral());
                    		cult.setIdCultivo(idCultivo);
                    	}
                    }
                    else{
                    	if(getJPanelDatosBienInmueble().getParent()!=null){
                    		IdCultivo idCultivo = new IdCultivo();
                    		ReferenciaCatastral refCatastral = new ReferenciaCatastral(getJTextFieldPCatastral1().getText(),getJTextFieldPCatastral2().getText());
                    		idCultivo.setParcelaCatastral(refCatastral.getRefCatastral());
                    		cult.setIdCultivo(idCultivo);
                    	}
                    }
    				                	                	
                    CultivoExtendedInfoDialog dialog = 
                        new CultivoExtendedInfoDialog(cult,isEditable);
                    dialog.setVisible(true);                        
                    
                    cult = dialog.getCultivo(cult);
                    
                    if (cult != null)
                    {
                        annadeCultivo(cult,false);
                    	loadData(cult);
                    }
                }
            });
            jButtonNuevoCultivo.setName("_nuevocultivo");
        }
        return jButtonNuevoCultivo;
    }
    
    /**
     * This method initializes jButtonEliminarCultivo	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonEliminarCultivo()
    {
        if (jButtonEliminarCultivo == null) {
            jButtonEliminarCultivo = new JButton();
            jButtonEliminarCultivo.setText(I18N.get("Expedientes","finca.boton.cultivo.eliminarcultivo"));
            jButtonEliminarCultivo.setName("_borrarcultivo");
            
            jButtonEliminarCultivo.addActionListener(new java.awt.event.ActionListener()
            {
		        public void actionPerformed(java.awt.event.ActionEvent e)
		        {
                    focoEn = "Cultivo";
                    componenteConFoco = jTextFieldSubparcela;      
                    focoAutomatico = true;
                    actualizaArbol(null);
                    if(cultivo!=null && cultivo.getCodSubparcela()!=null)
                    {
                        if (JOptionPane.showConfirmDialog(
                                (Component) FincaPanel.this,
                                I18N.get("Expedientes","fincapanel.removecultivoquestion"),
                                I18N.get("Expedientes","fincapanel.removecultivo"),
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                        {
                            eliminaCultivo();
						    cultivo = null;
                        }
		        	}
		        }
            });
                 
        }
        return jButtonEliminarCultivo;
    }
    
    public JPanel getJPanelCultivo()
    {                  
        if (jPanelCultivo == null)
        {         
            jPanelCultivo = new JPanel(new GridBagLayout());
            jPanelCultivo.setSize(new Dimension(400, 135));
            jPanelCultivo.setPreferredSize(new Dimension(400, 135));
            jPanelCultivo.setMaximumSize(jPanelCultivo.getPreferredSize());
            jPanelCultivo.setMinimumSize(jPanelCultivo.getPreferredSize());
            
            jPanelCultivo.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "finca.panel.cultivo.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelCultivo.setLayout(new GridBagLayout());            
            
            jPanelCultivo.add(getJPanelDatosCultivo(), 
                    new GridBagConstraints(0, 0, 1, 2, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.BOTH, new Insets (0,5,0,5), 0,0));
            /*jPanelCultivo.add(new JPanel(),
                    new GridBagConstraints(0, 1, 6, 1, 0.5, 0, GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));*/
            jPanelCultivo.add(getJPanelBotoneraCultivo(), 
                    new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.EAST,
                    		GridBagConstraints.NONE, new Insets (0,5,0,5), 0,0));
            focoPanelCultivo();                        
        }
        return jPanelCultivo;
    }
    
    public JPanel getJPanelDatosCultivo() {
        if (jPanelDatosCultivo == null) {			
            
            jLabelSubparcela = new JLabel("", JLabel.CENTER);
            jLabelSubparcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","finca.panel.cultivo.subparcela")));
            
            jLabelSuperficie = new JLabel("", JLabel.CENTER);
            jLabelSuperficie.setText(I18N.get("Expedientes","finca.panel.cultivo.superficie"));
            
            jLabelIntensidadProductiva = new JLabel("", JLabel.CENTER);
            jLabelIntensidadProductiva.setText(I18N.get("Expedientes","finca.panel.cultivo.intensidadproductiva"));
            
            jLabelTipoSubparcela = new JLabel("", JLabel.CENTER);
            jLabelTipoSubparcela.setText(I18N.get("Expedientes","finca.panel.cultivo.tiposubparcela"));
            
            jLabelTipoCultivo = new JLabel("", JLabel.CENTER);
            jLabelTipoCultivo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","finca.panel.cultivo.tipocultivo")));
            
            jLabelClase = new JLabel("", JLabel.CENTER);
            jLabelClase.setText(I18N.get("Expedientes","finca.panel.cultivo.clase"));
            
            jPanelDatosCultivo = new JPanel();
            jPanelDatosCultivo.setLayout(new GridBagLayout());
            jPanelDatosCultivo.setSize(new Dimension(400, 78));
            jPanelDatosCultivo.setPreferredSize(new Dimension(400, 78));
            jPanelDatosCultivo.setMaximumSize(jPanelDatosCultivo.getPreferredSize());
            jPanelDatosCultivo.setMinimumSize(jPanelDatosCultivo.getPreferredSize());
            //jPanelDatosCultivo.setPreferredSize(new Dimension(400, 80));
            
            jPanelDatosCultivo.add(jLabelSubparcela, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(getJTextFieldSubparcela(), 
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(jLabelTipoCultivo,
                    new GridBagConstraints(2, 0, 4, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosCultivo.add(new JPanel(),
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            jPanelDatosCultivo.add(getJComboBoxTipoCultivo(), 
                    new GridBagConstraints(2, 1, 4, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(jLabelTipoSubparcela, 
                    new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(getJComboBoxTipoSubparcela(), 
                    new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(jLabelIntensidadProductiva, 
                    new GridBagConstraints(2, 2, 2, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(getJComboBoxIntensidadProductiva(), 
                    new GridBagConstraints(2, 3, 2, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(jLabelClase, 
                    new GridBagConstraints(4, 2, 2, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(getJComboBoxClase(), 
                    new GridBagConstraints(4, 3, 2, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(jLabelSuperficie, 
                    new GridBagConstraints(6, 2, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            jPanelDatosCultivo.add(getJTextFieldSuperficieCultivo(), 
                    new GridBagConstraints(6, 3, 1, 1, 0.1, 0.1, 
                            GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL, new Insets (0,5,0,5), 0,0));
            
            EdicionUtils.enablePanel(jPanelDatosCultivo, isEditable);
            
            
        }
        return jPanelDatosCultivo;
    }
    
    public JPanel getJPanelBotoneraCultivo()
    {
        if (jPanelBotoneraCultivo == null)
        {
            
            jPanelBotoneraCultivo = new JPanel();
            //jPanelBotoneraCultivo.setPreferredSize(new Dimension(400, 30));
            //jPanelBotoneraCultivo.setMaximumSize(jPanelBotoneraCultivo.getPreferredSize());
            //jPanelBotoneraCultivo.setMinimumSize(jPanelBotoneraCultivo.getPreferredSize());
            
            jPanelBotoneraCultivo.setLayout(new GridBagLayout());
            jPanelBotoneraCultivo.add(getJButtonMasDatosCultivo(), 
                    new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));  
            jPanelBotoneraCultivo.add(getJButtonReparto(), 
                    new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));  
            jPanelBotoneraCultivo.add(getJButtonVistaLocal(), 
                    new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));  
            jPanelBotoneraCultivo.add(getJButtonNuevoCultivoComun(), 
                    new GridBagConstraints(5, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));  
            jPanelBotoneraCultivo.add(getJButtonNuevoCultivo(), 
                    new GridBagConstraints(6, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));  
            jPanelBotoneraCultivo.add(getJButtonEliminarCultivo(), 
                    new GridBagConstraints(7, 0, 1, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 3, 0, 0), 0, 0));  
            jPanelBotoneraCultivo.add(new JPanel(),
                    new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 3, 0, 0), 0, 0));        
        }
        return jPanelBotoneraCultivo;
    }
    
    /**
     * This method initializes jPanel	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
        }
        return jPanel;
    }
    
    private ComunidadBienes getComunidadBienesByNif(String nif)
    {
    	ArrayList lstComunidadBienes = bienInmuebleJuridico.getLstComBienes();
    	
    	if ((lstComunidadBienes != null)&&(nif.length() > 0))
    	{
    		for (int i=0;i<lstComunidadBienes.size();i++)
    		{
    			ComunidadBienes comunidadBienes = (ComunidadBienes)lstComunidadBienes.get(i);
    			if(comunidadBienes.getNif()!=null){
    				if (comunidadBienes.getNif().equalsIgnoreCase(nif))
    					return comunidadBienes;
    			}
    		}
    	}
    	return null;
    }
    
    public FincaCatastro recopilarDatosPanelFinca()
    {
        if (fincaEditada != null)
        {
      	  //No se ha borrado
      	
        	if(jComboBoxAnioPonencia.getSelectedItem()!=null){
        		/*if (jComboBoxAnioPonencia.getSelectedItem().toString().length() < 1)
        			fincaEditada.getDatosEconomicos().setAnioAprobacion(new Integer(0));
        		else if (jComboBoxAnioPonencia.getSelectedItem().toString().length() > 0)
        			fincaEditada.getDatosEconomicos().setAnioAprobacion(Integer.valueOf(jComboBoxAnioPonencia.getSelectedItem().toString()));
        	    */
                if (jComboBoxAnioPonencia.getSelectedItem().toString().length() > 0)
                    fincaEditada.getDatosEconomicos().setAnioAprobacion(Integer.valueOf(jComboBoxAnioPonencia.getSelectedItem().toString()));
            }
        	//else{
        	//	fincaEditada.getDatosEconomicos().setAnioAprobacion(new Integer(0));
        	//}
        	
        	if (jTextFieldFechaAlteracion.getText()!=null){
        		if (jTextFieldFechaAlteracion.getText().length() < 10)
        			fincaEditada.getDatosExpediente().setFechaAlteracion(new Date(0));
        		else if (jTextFieldFechaAlteracion.getText().length() == 10){        			
        			fincaEditada.getDatosExpediente().setFechaAlteracion(new Date(jTextFieldFechaAlteracion.getText()));
        		}
        	}
        	else{
        		fincaEditada.getDatosExpediente().setFechaAlteracion(new Date(0));
        	}
        	
	          DireccionLocalizacion direccionFinca = fincaEditada.getDirParcela();
	          if (direccionFinca != null)
	          {
	        	  if (jComboBoxTipoVia.getSelectedPatron()	!= null)
	        		  direccionFinca.setTipoVia(jComboBoxTipoVia.getSelectedPatron());
	        	  
	        	  if (jTextFieldCodigoVia.getText() != null){
	        		  Integer codigoVia = new Integer(jTextFieldCodigoVia.getText());
	        		  direccionFinca.setCodigoVia(codigoVia.intValue());
	        	  }
	        	  
	        	  if (jTextFieldVia.getText() != null)
	        		  direccionFinca.setNombreVia(jTextFieldVia.getText());
	        	 
	        	  if(jComboBoxPoligono.getSelectedItem() != null){
	        		  direccionFinca.setCodPoligono(jComboBoxPoligono.getSelectedItem().toString());
	        	  }
	        	  if (jTextFieldParcela.getText() != null)
	        		  direccionFinca.setCodParcela(jTextFieldParcela.getText());
	        	  //if (jTextFieldParcela.getText() != null)
	        	  if(paraje!=null && paraje.getCodigo()!=null)
	        		  direccionFinca.setCodParaje(paraje.getCodigo());	
	           	  //if (jTextFieldParaje.getText() != null)
	        	  if(paraje!=null && paraje.getNombre()!=null)
	        		  //direccionFinca.setCodParaje(jTextFieldParaje.getText());
	           		direccionFinca.setNombreParaje(paraje.getNombre());
	        	  if (jTextFieldNumero.getText().length() > 0)
	        		  direccionFinca.setPrimerNumero(Integer.parseInt(jTextFieldNumero.getText()));
	        	  else if (jTextFieldNumero.getText().length() < 1)
	        		  direccionFinca.setPrimerNumero(-1);
		          fincaEditada.setDirParcela(direccionFinca);
	          }
	          DatosFisicosFinca fisicos = fincaEditada.getDatosFisicos();
	          if (fisicos != null)
	          {
		          if (jTextFieldSupTotal.getText().length() > 0)
		        	  fisicos.setSupTotal(new Long(jTextFieldSupTotal.getText()));
		          else if (jTextFieldSupTotal.getText().length() < 1)
		        	  fisicos.setSupTotal(new Long(0));
		          if (jTextFieldSupSobre.getText().length() > 0)
		        	  fisicos.setSupSobreRasante(new Long(jTextFieldSupSobre.getText()));
		          else if (jTextFieldSupSobre.getText().length() < 1)
		        	  fisicos.setSupSobreRasante(new Long(0));
		          if (jTextFieldSupCubierta.getText().length() > 0)
		        	  fisicos.setSupCubierta(new Long(jTextFieldSupCubierta.getText()));
		          else if (jTextFieldSupCubierta.getText().length() < 1)
		        	  fisicos.setSupCubierta(new Long(0));
		          if (jTextFieldSupSolar.getText().length() > 0)
		        	  fisicos.setSupFinca(new Long(jTextFieldSupSolar.getText()));
		          else if (jTextFieldSupSolar.getText().length() < 1)
		        	  fisicos.setSupFinca(new Long(0));
                  
                  //JAVIER se recoge la info de sup bajo rasante
                  if (SupBajoRasante != null && SupBajoRasante.length() > 0)
                      fisicos.setSupBajoRasante(new Long(SupBajoRasante));
                  else 
                      fisicos.setSupBajoRasante(new Long(0));

                  fincaEditada.setDatosFisicos(fisicos);
	          }
        }
        return fincaEditada;
    }
    
    public BienInmuebleJuridico recopilarDatosPanelBienInmueble()
    {
    	 ReferenciaCatastral refFinca = new ReferenciaCatastral((String)jTextFieldPCatastral1.getText(), (String)jTextFieldPCatastral2.getText());
    	 
    	 IdBienInmueble idBienInmueble = null;
         if ((jTextFieldCargo.getText().length() > 0) &&(jTextFieldDC1.getText().length() > 0) && (jTextFieldDC2.getText().length() > 0))
         {
	       	  idBienInmueble = new IdBienInmueble();
	       	  idBienInmueble.setNumCargo(jTextFieldCargo.getText());
	       	  idBienInmueble.setParcelaCatastral(refFinca.getRefCatastral());
	       	  idBienInmueble.setDigControl1(jTextFieldDC1.getText());
	       	  idBienInmueble.setDigControl2(jTextFieldDC2.getText());
         }
         
         if (idBienInmueble != null)
           	 bienInmuebleJuridico.getBienInmueble().setIdBienInmueble(idBienInmueble);
                    
         if ((bienInmuebleJuridico !=  null))
         {
       	  //No se ha eliminado el BI
       	  if (bienInmuebleJuridico.getBienInmueble().getDomicilioTributario() == null)
       		bienInmuebleJuridico.getBienInmueble().setDomicilioTributario(new DireccionLocalizacion());
             DireccionLocalizacion direccion = bienInmuebleJuridico.getBienInmueble().getDomicilioTributario();
             if (jTextFieldBloque.getText() != null)
           	  direccion.setBloque(jTextFieldBloque.getText());
            
             if (jComboBoxEscalera.getSelectedItem()!= null)
              	  direccion.setEscalera(((EstructuraDB)jComboBoxEscalera.getSelectedItem()).getPatron());
            
             if (jComboBoxPlanta.getSelectedItem()!= null)
              	  direccion.setPlanta(((EstructuraDB)jComboBoxPlanta.getSelectedItem()).getPatron());
             
             if (jComboBoxPuerta.getSelectedItem() != null)
            	 direccion.setPuerta(((EstructuraDB)jComboBoxPuerta.getSelectedItem()).getPatron());
             //Camibie estas tres lineas de bienInmueblePanel
             bienInmuebleJuridico.getBienInmueble().setDomicilioTributario(direccion);
             if (jTextFieldNumOrdenDH.getText() != null)
            	 bienInmuebleJuridico.getBienInmueble().getDatosEconomicosBien().setNumOrdenHorizontal(jTextFieldNumOrdenDH.getText());
             if (jTextFieldCoefParticipacion.getText().length() > 0)
            	 bienInmuebleJuridico.getBienInmueble().getDatosEconomicosBien().setCoefParticipacion(Float.valueOf(jTextFieldCoefParticipacion.getText()));
//             else if (jTextFieldCoefParticipacion.getText().length() < 1)
//            	 bienInmuebleJuridico.getBienInmueble().getDatosEconomicosBien().setCoefParticipacion(new Float(0));
         }
         return bienInmuebleJuridico;
    }
    
    public Titular recopilarDatosPanelTitular()
    {
    	if (jTextFieldNif.getText().length() > 0)
    		titular.setNif(jTextFieldNif.getText());
    		
    	if (titular != null)
        {
    		//No se ha borrado
    		if (jTextFieldRazonSocial.getText() != null)
    			titular.setRazonSocial(jTextFieldRazonSocial.getText());
    		if (jTextFieldNifConyuge.getText() != null)
    			titular.setNifConyuge(jTextFieldNifConyuge.getText());
		    if(titular.getDerecho()==null)
            {
                Derecho der = new Derecho();
                titular.setDerecho(der);
            }
            titular.getDerecho().setCodDerecho(jComboBoxDerecho.getSelectedPatron());
            try
            {
            	if (!jTextFieldPorcentaje.getText().equals(""))
            		titular.getDerecho().setPorcentajeDerecho(Float.parseFloat(jTextFieldPorcentaje.getText()));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            if (jTextFieldFechaAlteracionTitular.getText()!=null){
        		if (jTextFieldFechaAlteracionTitular.getText().length() < 10)
        			titular.setFechaAlteracion("");
        		else if (jTextFieldFechaAlteracionTitular.getText().length() == 10){        			
        			titular.setFechaAlteracion(jTextFieldFechaAlteracionTitular.getText());
        		}
        	}
        	else{
        		titular.setFechaAlteracion("");
        	}
            
            //Comunidad de bienes
	        if (jComboBoxComunidadBienes.getSelectedItem() != null && jComboBoxComunidadBienes.getSelectedItem() instanceof ComunidadBienes)
	        {
	            ComunidadBienes comunidad =  (ComunidadBienes)jComboBoxComunidadBienes.getSelectedItem();
	            if (comunidad != null)
	            {	            	
	            	if (jTextFieldComunidadBienes.getText() != null){
	            		comunidad.setComplementoTitularidad(jTextFieldComunidadBienes.getText());
	            	}
	            	if (jComboBoxComunidadBienes.getSelectedItem() !=  null){
	            		titular.setNifCb(comunidad.getNif());
	            	}
	            	ArrayList lstComunidad =  bienInmuebleJuridico.getLstComBienes();
                    if(lstComunidad !=null) {

                        if (!comunidadBienesExiste(lstComunidad, comunidad)){
                            lstComunidad.add(comunidad);
                        }

                        bienInmuebleJuridico.setLstComBienes(lstComunidad);
                    }
                }
	        }
        }
    	return titular;
    }
    
    private boolean comunidadBienesExiste(ArrayList lstComunidadBienes, ComunidadBienes comunidad){
    	
    	Iterator itComBienes = lstComunidadBienes.iterator();
        
        while (itComBienes.hasNext())
        {
            ComunidadBienes comunidadBienes = ((ComunidadBienes)itComBienes.next()); 
            if (comunidad.getNif()!= null && comunidadBienes.getNif()!= null
            		&& comunidad.getNif().equals(comunidadBienes.getNif())){
            	return true;
            }
        }            
    	
    	return false;
    }
    
    public Cultivo recopilarDatosPanelCultivo()
    {
    	 ReferenciaCatastral refFinca = new ReferenciaCatastral((String)jTextFieldRefCatastral1.getText(), (String)jTextFieldRefCatastral2.getText());
    	
    	IdCultivo idCultivo;
    	if ((jTextFieldCargo.getText() != null) && (jTextFieldSubparcela.getText() != null))
    	{
    		idCultivo = new IdCultivo();
    		idCultivo.setParcelaCatastral(refFinca.getRefCatastral());
    		idCultivo.setNumOrden(jTextFieldCargo.getText());
    		cultivo.setIdCultivo(idCultivo);
    		cultivo.setCodSubparcela(jTextFieldSubparcela.getText());
    	}
    	
       if (cultivo != null)                              
        {                                       
      	  //No se ha borrado
    	    if (jComboBoxTipoCultivo.getSelectedItem() != null && ((EstructuraDB)jComboBoxTipoCultivo.getSelectedItem()).getPatron()!=null)
  	        	cultivo.getIdCultivo().setCalifCultivo(((EstructuraDB)jComboBoxTipoCultivo.getSelectedItem()).getPatron());
  	        if (jComboBoxTipoSubparcela.getSelectedItem() != null && jComboBoxTipoSubparcela.getSelectedPatron()!=null)
  	        	cultivo.setTipoSubparcela(jComboBoxTipoSubparcela.getSelectedPatron());
  	        if (jComboBoxIntensidadProductiva.getSelectedItem() != null){
  	        	if(!jComboBoxIntensidadProductiva.getSelectedItem().toString().equals(""))
  	        		cultivo.setIntensidadProductiva(new Integer(jComboBoxIntensidadProductiva.getSelectedItem().toString()));
  	        	else
  	        		cultivo.setIntensidadProductiva(new Integer(0));
  	        }
  	        if (jTextFieldSuperficieCultivo.getText().length() > 0)
  	        	cultivo.setSuperficieParcela(Long.valueOf(jTextFieldSuperficieCultivo.getText()));
  	        else if (jTextFieldSuperficieCultivo.getText().length() < 1)
  	        	cultivo.setSuperficieParcela(new Long(0));
  	        if(jComboBoxClase.getSelectedItem()!=null && ((ComboBoxEstructuras)jComboBoxClase).getSelectedPatron()!=null)
  	        	cultivo.setTipoSuelo(((ComboBoxEstructuras)jComboBoxClase).getSelectedPatron());

            if (fincaEditada.getCodDelegacionMEH() != null)
                cultivo.setCodDelegacionMEH(fincaEditada.getCodDelegacionMEH());
            if (fincaEditada.getCodMunicipioDGC() != null)
                cultivo.setCodMunicipioDGC(fincaEditada.getCodMunicipioDGC());            
        }
        return cultivo;
    }
    
    public ConstruccionCatastro recopilarDatosPanelConstruccion()
    {
    	ReferenciaCatastral refFinca = new ReferenciaCatastral((String)jTextFieldRefCatastral1.getText(), (String)jTextFieldRefCatastral2.getText());
    	
    	if (jTextFieldIdLocal.getText().length() > 0)
    		construccion.setIdConstruccion(refFinca.getRefCatastral() + jTextFieldIdLocal.getText());
    	
        if (construccion != null)
         {

              construccion.setRefParcela(new ReferenciaCatastral(refFinca.getRefCatastral()));
	          DireccionLocalizacion direccionConstruccion = construccion.getDomicilioTributario();
              construccion.setNumOrdenConstruccion(jTextFieldIdLocal.getText());
              construccion.getDatosFisicos().setCodUnidadConstructiva(jTextFieldUnidadConstrLocal.getText());
              if (jTextFieldBloqueLocal.getText() != null)
	        	  direccionConstruccion.setBloque(jTextFieldBloqueLocal.getText());
	         
	          if (jComboBoxEscaleraLocal.getSelectedItem() != null)
	        	  direccionConstruccion.setEscalera(((EstructuraDB)jComboBoxEscaleraLocal.getSelectedItem()).getPatron());
	          
	          if (jComboBoxPlantaLocal.getSelectedItem() != null)
	        	  direccionConstruccion.setPlanta(((EstructuraDB)jComboBoxPlantaLocal.getSelectedItem()).getPatron());
	         
	          if (jComboBoxPuertaLocal.getSelectedItem() != null)
	        	  direccionConstruccion.setPuerta(((EstructuraDB)jComboBoxPuertaLocal.getSelectedItem()).getPatron());
	          
	          construccion.setDomicilioTributario(direccionConstruccion);
	          
	          if (jTextFieldSupTotalLocal.getText().length() > 0)
	        	  construccion.getDatosFisicos().setSupTotal(Long.valueOf(jTextFieldSupTotalLocal.getText()));
	          else if (jTextFieldSupTotalLocal.getText().length() < 1)
	        	  construccion.getDatosFisicos().setSupTotal(new Long(0));
	          if (jTextFieldSupTerrazaLocal.getText().length() > 0)
	        	  construccion.getDatosFisicos().setSupTerrazasLocal(Long.valueOf(jTextFieldSupTerrazaLocal.getText()));
	          else if (jTextFieldSupTerrazaLocal.getText().length() < 1)
	        	  construccion.getDatosFisicos().setSupTerrazasLocal(new Long(0));
	          if (jComboBoxDestinoLocal.getSelectedItem() != null){
	        	  construccion.getDatosFisicos().setCodDestino( ((EstructuraDB)jComboBoxDestinoLocal.getSelectedItem()).getPatron());
	          }
	          construccion.getDatosEconomicos().setCodCategoriaPredominante(jComboBoxCategoria.getSelectedPatron());
	          construccion.getDatosEconomicos().setCodTipoValor(jComboBoxCodTipoValor.getSelectedPatron());
	          if (jComboBoxTipologiaLocal.getSelectedItem()!=null)
	        	  construccion.getDatosEconomicos().setTipoConstruccion(((EstructuraDB)jComboBoxTipologiaLocal.getSelectedItem()).getPatron());
	
         } 
        return construccion;
    }
    
    
    public void clearPanel(Component c)
    {
        if (c instanceof JTextField)
            ((JTextField)c).setText("");
        else if (c instanceof JComboBox)
        	if(((JComboBox)c).getItemCount()>0)
        		((JComboBox)c).setSelectedIndex(0);
                
        if (c instanceof Container)
        {
            Component[] arrComp = ((Container)c).getComponents();
            for (int i=0; i<arrComp.length; i++)
                clearPanel(arrComp[i]);
        }            
        
    }
    
    /**
     * @return Returns the bienInmueble.
     */
    public BienInmuebleJuridico getBienInmueble()
    {
        return bienInmuebleJuridico;
    }
    
    /**
     * @param bienInmueble The bienInmueble to set.
     */
    public void setBienInmueble(BienInmuebleJuridico bienInmueble)
    {
        this.bienInmuebleJuridico = bienInmueble;
    }
    
    /**
     * @return Returns the construccion.
     */
    public ConstruccionCatastro getConstruccion()
    {
        return construccion;
    }
    
    /**
     * @param construccion The construccion to set.
     */
    public void setConstruccion(ConstruccionCatastro construccion)
    {
        this.construccion = construccion;
    }
    
    /**
     * @return Returns the cultivo.
     */
    public Cultivo getCultivo()
    {
        return cultivo;
    }
    
    /**
     * @param cultivo The cultivo to set.
     */
    public void setCultivo(Cultivo cultivo)
    {
        this.cultivo = cultivo;
    }
    
    /**
     * @return Returns the titular.
     */
    public Titular getTitular()
    {
        return titular;
    }
    
    /**
     * @param titular The titular to set.
     */
    public void setTitular(Titular titular)
    {
        this.titular = titular;
    }
    
    /**
     * @return Returns the fincaCatastro.
     */
    public FincaCatastro getFincaCatastro()
    {
        return fincaEditada;
    }
    
    /**
     * @param fincaCatastro The fincaCatastro to set.
     */
    public void setFincaCatastro(FincaCatastro fincaCatastro)
    {
        this.fincaEditada = fincaCatastro;
    }
    
    /**
     * This method initializes jScrollPaneBienInmueble  
     *  
     * @return javax.swing.JScrollPane  
     */
    public JScrollPane getJScrollPaneBienInmueble()
    {
        if (jScrollPaneBienInmueble == null)
        {
            jScrollPaneBienInmueble = new JScrollPane();
            //jScrollPaneBienInmueble.setPreferredSize(new Dimension(400, 100));
            //jScrollPaneBienInmueble.setMaximumSize(jScrollPaneBienInmueble.getPreferredSize());
            //jScrollPaneBienInmueble.setMinimumSize(jScrollPaneBienInmueble.getPreferredSize());
            jScrollPaneBienInmueble.setViewportView(getJTableBienInmueble());
            
        }
        return jScrollPaneBienInmueble;
    }
    
    /**
     * This method initializes jTableBienInmueble   
     *  
     * @return javax.swing.JTable   
     */
    private JTable getJTableBienInmueble()
    {
        if (jTableBienInmueble == null)
        {
            jTableBienInmueble = new JTable();
            
            tablebimodel = new TableBienInmuebleCatastroModel();
            
            TableSorted tblSorted= new TableSorted(tablebimodel);
            tblSorted.setTableHeader(jTableBienInmueble.getTableHeader());
            jTableBienInmueble.setModel(tblSorted);
            jTableBienInmueble.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableBienInmueble.setCellSelectionEnabled(false);
            jTableBienInmueble.setColumnSelectionAllowed(false);
            jTableBienInmueble.setRowSelectionAllowed(true);
            jTableBienInmueble.getTableHeader().setReorderingAllowed(false);
            
            ArrayList lst = new ArrayList();
            lst.add(new BienInmuebleCatastro());
            ((TableBienInmuebleCatastroModel)((TableSorted)jTableBienInmueble.getModel()).
                    getTableModel()).setData(lst);
            
        }
        return jTableBienInmueble;
    }
    
    public void loadTableTitular(ArrayList lstTit)
    {
        ((TablePersonaModel)((TableSorted)getJTableTitular().getModel()).
                getTableModel()).setData(lstTit);     
    }
    
    public void loadTableFinca(ArrayList lstFinc)
    {
        ((TableFincaCatastroModel)((TableSorted)getJTableFincas().getModel()).
                getTableModel()).setData(lstFinc);     
    }
    
    public void loadTableLocal(ArrayList lstConst)
    {
        ((TableLocalModel)((TableSorted)getJTableLocal().getModel()).
                getTableModel()).setData(lstConst);     
    }
    
    public void loadTableCultivo(ArrayList lstCult)
    {
        ((TableCultivoModel)((TableSorted)getJTableCultivo().getModel()).
                getTableModel()).setData(lstCult);     
    }
    
    public void loadTableBienes(ArrayList lstBienesInmuebles)
    {
        ((TableBienInmuebleCatastroModel)((TableSorted)getJTableBienInmueble().getModel()).
                getTableModel()).setData(lstBienesInmuebles);   
    }
    
    /**
     * @return Returns the isEditable.
     */
    public boolean isEditable()
    {
        return isEditable;
    }
    
    /**
     * @param isEditable The isEditable to set.
     */
    public void setEditable(boolean isEditable)
    {
        this.isEditable = isEditable;
    }

    public void enter()
    {       	
    	if (this.gestionExpedientePanel != null){
    		this.gestionExpedientePanel.getJButtonValidar().setEnabled(true);
    		
    		if(this.gestionExpedientePanel.getExpediente().getIdEstado() >= ConstantesRegExp.ESTADO_FINALIZADO)
            {
                EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), false);   
                this.gestionExpedientePanel.getJButtonEstadoModificado().setEnabled(true);
            }
    		else{
    			EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), true);
    			this.gestionExpedientePanel.getJButtonEstadoModificado().setEnabled(false);
    		}
    	}
    }

    public void exit()
    {        
    }   
    
    private void focoPanelPantalla()
    {
        jTextFieldRefCatastral1.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldRefCatastral1;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldRefCatastral2.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldRefCatastral2;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldFechaAlteracion.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldFechaAlteracion;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });        
        jComboBoxTipoVia.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxTipoVia;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();                    
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldVia.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldVia;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldNumero.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldNumero;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        
        jComboBoxAnioPonencia.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxAnioPonencia;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        
        jComboBoxPoligono.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxPoligono;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldParcela.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldParcela;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldParaje.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldParaje;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSupSolar.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSupSolar;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSupTotal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSupTotal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSupSobre.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSupSobre;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSupCubierta.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSupCubierta;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxTipoVia.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Parcela"))
                {
                    focoEn = "Parcela";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxTipoVia;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
    }

    private void focoPanelBI()
    {
        jTextFieldPCatastral1.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldPCatastral1;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldPCatastral2.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldPCatastral2;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldCargo.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldCargo;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldDC1.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldDC1;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldDC2.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldDC2;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();                    
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldBloque.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldBloque;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        
        jComboBoxEscalera.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxEscalera;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
       
        jComboBoxPlanta.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxPlanta;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
       
        jComboBoxPuerta.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxPuerta;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldNumOrdenDH.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldNumOrdenDH;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldCoefParticipacion.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("BI"))
                {
                    focoEn = "BI";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldCoefParticipacion;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
    }

    private void focoPanelLocal()
    {
        jTextFieldIdLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldIdLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();                    
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldUnidadConstrLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldUnidadConstrLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldBloqueLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldBloqueLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        
        jComboBoxEscaleraLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxEscaleraLocal;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        
        jComboBoxPlantaLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxPlantaLocal;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        
        jComboBoxPuertaLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxPuertaLocal;
                    actualizaArbol(null);
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxDestinoLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxDestinoLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSupTotalLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSupTotalLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSupTerrazaLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSupTerrazaLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxTipologiaLocal.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxTipologiaLocal;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxCategoria.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxCategoria;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxCodTipoValor.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Local"))
                {
                    focoEn = "Local";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxCodTipoValor;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
    }

    private void focoPanelCultivo()
    {
        jTextFieldSubparcela.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Cultivo"))
                {
                    focoEn = "Cultivo";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSubparcela;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxTipoCultivo.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Cultivo"))
                {
                    focoEn = "Cultivo";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxTipoCultivo;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxTipoSubparcela.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Cultivo"))
                {
                    focoEn = "Cultivo";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxTipoSubparcela;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jComboBoxIntensidadProductiva.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Cultivo"))
                {
                    focoEn = "Cultivo";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxIntensidadProductiva;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldSuperficieCultivo.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Cultivo"))
                {
                    focoEn = "Cultivo";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldSuperficieCultivo;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
    }

    private void focoEnPersona()
    {
        jTextFieldNif.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldNif;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });       
        jTextFieldRazonSocial.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldRazonSocial;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        /*jComboBoxComunidadBienes.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxComunidadBienes;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });*/
        jTextFieldComunidadBienes.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {

            }
            public void focusLost(FocusEvent evt)
            {
                FincaPanel.this.comunidadbienes.setRazonSocial(jTextFieldComunidadBienes.getText());
            }
        });
        jComboBoxDerecho.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jComboBoxDerecho;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldPorcentaje.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldPorcentaje;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldNifConyuge.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldNifConyuge;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });
        jTextFieldFechaAlteracionTitular.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent evt)
            {
                if(focoEn ==null ||!focoEn.equalsIgnoreCase("Persona"))
                {
                    focoEn = "Persona";
                    focoAutomatico = true;
                    componenteConFoco = jTextFieldFechaAlteracionTitular;
                    actualizaArbol(null);
                }
                if(focoManual)
                {
                    focoManual = false;
                    focoEn = null;
                    FocusManager.getCurrentManager().clearGlobalFocusOwner();
                }
            }
            public void focusLost(FocusEvent evt)
            {
            }
        });

    }

    private void actualizaArbol(HideableNode nodoNuevo)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
        Object nodeInfo = node.getUserObject();
        if(nodoNuevo!=null)
        {
            TreePath path = new TreePath(nodoNuevo.getPath());
            arbol.setSelectionPath(path);
        }
        else if(focoEn.equalsIgnoreCase("Parcela"))
        {
            if(nodeInfo instanceof BienInmuebleJuridico)
            {
                arbol.setSelectionPath(arbol.getSelectionPath().getParentPath());
            }
            else if(nodeInfo instanceof Persona || nodeInfo instanceof Cultivo || nodeInfo instanceof ConstruccionCatastro)
            {
                if(((DefaultMutableTreeNode)arbol.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject() instanceof BienInmuebleJuridico)
                {
                    arbol.setSelectionPath(arbol.getSelectionPath().getParentPath().getParentPath());
                }
                else
                {
                    arbol.setSelectionPath(arbol.getSelectionPath().getParentPath());                    
                }
            }
        }
        else if(focoEn.equalsIgnoreCase("BI"))
        {
            if(nodeInfo instanceof FincaCatastro)
            {
                boolean continua = true;
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
            }
            else if(nodeInfo instanceof Persona || nodeInfo instanceof Cultivo || nodeInfo instanceof ConstruccionCatastro)
            {
                if(((DefaultMutableTreeNode)arbol.getSelectionPath().getParentPath().getLastPathComponent()).getUserObject()
                        instanceof BienInmuebleJuridico)
                {
                    arbol.setSelectionPath(arbol.getSelectionPath().getParentPath());
                }
                else
                {
                    boolean continua = true;
                    DefaultMutableTreeNode nodeP = (DefaultMutableTreeNode)arbol.getSelectionPath().getParentPath().getLastPathComponent();
                    for (Enumeration e=nodeP.children(); e.hasMoreElements() && continua; )
                    {
                        HideableNode n = (HideableNode)e.nextElement();
                        if(n.getUserObject() instanceof BienInmuebleJuridico)
                        {
                            TreePath path = new TreePath(n.getPath());
                            arbol.setSelectionPath(path);
                            continua = false;
                        }
                    }                    
                }
            }
        }
        else if(focoEn.equalsIgnoreCase("Local"))
        {
            boolean continua = true;
            TreeNode nodeAux = null;
            if(nodeInfo instanceof FincaCatastro)
            {
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        for (Enumeration e2=n.children(); e2.hasMoreElements() && continua; )
                        {
                            HideableNode n2 = (HideableNode)e2.nextElement();
                            if(n2.getUserObject() instanceof ConstruccionCatastro)
                            {
                                TreePath path = new TreePath(n2.getPath());
                                arbol.setSelectionPath(path);
                                continua = false;
                            }
                        }
                    }
                    if(continua && n.getUserObject() instanceof ConstruccionCatastro)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                continua = false;
            }
            else if(nodeInfo instanceof BienInmuebleJuridico)
            {
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof ConstruccionCatastro)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                nodeAux= node.getParent();
            }
            else if(nodeInfo instanceof Persona || nodeInfo instanceof Cultivo)
            {
                for (Enumeration e=node.getParent().children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof ConstruccionCatastro)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                nodeAux = node.getParent().getParent();
            }
            if(continua && nodeAux!=null)
            {
                for (Enumeration e=nodeAux.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        for (Enumeration e2=n.children(); e2.hasMoreElements() && continua; )
                        {
                            HideableNode n2 = (HideableNode)e2.nextElement();
                            if(n2.getUserObject() instanceof ConstruccionCatastro)
                            {
                                TreePath path = new TreePath(n2.getPath());
                                arbol.setSelectionPath(path);
                                continua = false;
                            }
                        }
                    }
                    if(continua && n.getUserObject() instanceof ConstruccionCatastro)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
            }
        }
        else if(focoEn.equalsIgnoreCase("Cultivo"))
        {
            boolean continua = true;
            TreeNode nodeAux = null;
            if(nodeInfo instanceof FincaCatastro)
            {
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        for (Enumeration e2=n.children(); e2.hasMoreElements() && continua; )
                        {
                            HideableNode n2 = (HideableNode)e2.nextElement();
                            if(n2.getUserObject() instanceof Cultivo)
                            {
                                TreePath path = new TreePath(n2.getPath());
                                arbol.setSelectionPath(path);
                                continua = false;
                            }
                        }
                    }
                    if(continua && n.getUserObject() instanceof Cultivo)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                continua = false;
            }
            else if(nodeInfo instanceof BienInmuebleJuridico)
            {
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof Cultivo)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                nodeAux = node.getParent();
            }
            else if(nodeInfo instanceof Persona || nodeInfo instanceof ConstruccionCatastro)
            {
                for (Enumeration e=node.getParent().children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof Cultivo)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                nodeAux=node.getParent().getParent();
            }
            if(continua && nodeAux!=null)
            {
                for (Enumeration e=nodeAux.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        for (Enumeration e2=n.children(); e2.hasMoreElements() && continua; )
                        {
                            HideableNode n2 = (HideableNode)e2.nextElement();
                            if(n2.getUserObject() instanceof Cultivo)
                            {
                                TreePath path = new TreePath(n2.getPath());
                                arbol.setSelectionPath(path);
                                continua = false;
                            }
                        }
                    }
                    if(continua && n.getUserObject() instanceof Cultivo)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
            }
        }
        else if(focoEn.equalsIgnoreCase("Persona"))
        {
            boolean continua = true;
            TreeNode nodeAux = null;
            if(nodeInfo instanceof FincaCatastro)
            {
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        for (Enumeration e2=n.children(); e2.hasMoreElements() && continua; )
                        {
                            HideableNode n2 = (HideableNode)e2.nextElement();
                            if(n2.getUserObject() instanceof Persona)
                            {
                                TreePath path = new TreePath(n2.getPath());
                                arbol.setSelectionPath(path);
                                continua = false;
                            }
                        }
                    }
                }
                continua = false;
            }
            else if(nodeInfo instanceof BienInmuebleJuridico)
            {
                for (Enumeration e=node.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof Persona)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                nodeAux = node.getParent();
            }
            else if(nodeInfo instanceof ConstruccionCatastro || nodeInfo instanceof Cultivo)
            {
                for (Enumeration e=node.getParent().children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof Persona)
                    {
                        TreePath path = new TreePath(n.getPath());
                        arbol.setSelectionPath(path);
                        continua = false;
                    }
                }
                nodeAux = node.getParent().getParent();
            }
            if(continua && nodeAux!=null)
            {
                for (Enumeration e=nodeAux.children(); e.hasMoreElements() && continua; )
                {
                    HideableNode n = (HideableNode)e.nextElement();
                    if(n.getUserObject() instanceof BienInmuebleJuridico)
                    {
                        for (Enumeration e2=n.children(); e2.hasMoreElements() && continua; )
                        {
                            HideableNode n2 = (HideableNode)e2.nextElement();
                            if(n2.getUserObject() instanceof Persona)
                            {
                                TreePath path = new TreePath(n2.getPath());
                                arbol.setSelectionPath(path);
                                continua = false;
                            }
                        }
                    }
                }
            }
        }
        focoAutomatico = false;
        if(componenteConFoco!=null)
        {
            componenteConFoco.requestFocusInWindow();
        }
        
    }


    public void setPanelExpedientes(ExpedientePanelTree panelExpedientes)
    {
        this.panelExpedientes = panelExpedientes;
        arbol= panelExpedientes.getTree();
    }
    
    public void asignaArbolPanel(Object panel)
    {
    	if (panel instanceof com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree) {
			arbol= ((com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree)panel).getTree();
		}
    	else if (panel instanceof com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree){
    		arbol= ((com.geopista.ui.plugin.infcattitularidad.paneles.InfoCatastralPanelTree)panel).getTree();
    	}
    }
    

    private void compruebaEliminarFinca()
    {
        if (compruebaNumeroFincaReales(true))
        {
            try
            {

            	panelExpedientes.eliminaFincaOBI(((HideableNode)arbol.getSelectionPath().getLastPathComponent()).getUserObject());

            	if (((HideableNode)arbol.getSelectionPath().getLastPathComponent()).getUserObject() instanceof FincaCatastro){
            		
            		Enumeration enumChildren = ((HideableNode)arbol.getSelectionPath().getLastPathComponent()).children();

            		if (enumChildren != null){
            			Object object = (Object) enumChildren.nextElement();
            			if (object != null){
            				panelExpedientes.removeObject(object);
            				enumChildren = ((HideableNode)arbol.getSelectionPath().getLastPathComponent()).children();

            				while (enumChildren.hasMoreElements())
            				{
            					object = (Object) enumChildren.nextElement();
            					panelExpedientes.removeObject(object);
            					enumChildren = ((HideableNode)arbol.getSelectionPath().getLastPathComponent()).children();
            				}
            			}
            		}
            	}

            	TreePath path = new TreePath(arbol.getSelectionPath().getLastPathComponent());
            	arbol.setSelectionPath(path);
            }
            catch (Exception e)
            {
            	e.printStackTrace();
                //El nodo no se ha encontrado en el arbol, es decir, ya se ha borrado
            }
        }
        else
        {
            JOptionPane
            .showMessageDialog(
                    this,
                    I18N.get("Expedientes", "Error.M15"),
                    "Error.M15",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void compruebaEliminarBI()
    {
        if(fincaEditada == null)
        {
            if (compruebaNumeroFincaReales(false))
            {
                try
                {

                    panelExpedientes.eliminaFincaOBI(bienInmuebleJuridico);
                    panelExpedientes.removeObject(arbol.getSelectionPath().getLastPathComponent());
                    TreePath path = new TreePath(arbol.getModel().getRoot());
                    arbol.setSelectionPath(path);
                }
                catch (Exception e)
                {
                    //El nodo no se ha encontrado en el arbol, es decir, ya se ha borrado
                    e.printStackTrace();
                }
            }
            else
            {
                JOptionPane
                .showMessageDialog(
                        this,
                        I18N.get("Expedientes", "Error.M16"),
                        "Error.M16",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        else
        {
            ArrayList lstBi = fincaEditada.getLstBienesInmuebles();
            boolean continua = true;
            for(int i=0; i< lstBi.size() && continua;i++)
            {
                if(((BienInmuebleJuridico)lstBi.get(i)).getBienInmueble().getIdBienInmueble().getIdBienInmueble().
                        equalsIgnoreCase(bienInmuebleJuridico.getBienInmueble().getIdBienInmueble().getIdBienInmueble()))
                {
                    lstBi.remove(i);
                    continua = false;
                }
            }
            if(!continua)
            {
                TreePath path = arbol.getSelectionPath().getParentPath();
                panelExpedientes.removeObject(arbol.getSelectionPath().getLastPathComponent());
                arbol.setSelectionPath(path);
            }
        }
    }

    private boolean compruebaNumeroFincaReales(boolean isFinca)
    {
        if(isFinca)
        {
            try
            {
                Object parcela = ConstantesRegExp.clienteCatastro.existeParcelaEnBD(fincaEditada.getRefFinca().
                        getRefCatastral());
                if (parcela == null)
                {
                    return true;
                }
                else
                {
                    int numParcelarReales = 0;
                    ArrayList lstParcelas = panelExpedientes.getLstReferencias();
                    for(int i= 0; i<lstParcelas.size();i++)
                    {
                        FincaCatastro finca = (FincaCatastro)lstParcelas.get(i);
                        parcela = ConstantesRegExp.clienteCatastro.existeParcelaEnBD(finca.getRefFinca().
                        getRefCatastral());
                        if(parcela!=null)
                        {
                            numParcelarReales++;    
                        }
                    }
                    return numParcelarReales > 1;
                }
            }
            catch (Exception e1)
            {
                return false;
            }
        }
        else
        {
            try
            {
                Object bi = ConstantesRegExp.clienteCatastro.existeBIEnBD(bienInmuebleJuridico.
                        getBienInmueble().getIdBienInmueble().getIdBienInmueble());
                if (bi == null)
                {
                    return true;
                }
                else
                {
                    int numParcelarReales = 0;
                    ArrayList lstParcelas = panelExpedientes.getLstReferencias();
                    for(int i= 0; i<lstParcelas.size();i++)
                    {
                        BienInmuebleJuridico bij = (BienInmuebleJuridico)lstParcelas.get(i);
                        bi = ConstantesRegExp.clienteCatastro.existeBIEnBD(bij.
                        getBienInmueble().getIdBienInmueble().getIdBienInmueble());
                        if(bi!=null)
                        {
                            numParcelarReales++;
                        }
                    }
                    if(numParcelarReales>1)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            catch (Exception e1)
            {
                return false;
            }
        }
    }

    public void annadeBI(BienInmuebleJuridico bij) {
        if(fincaEditada!=null){
            ArrayList lstBI = fincaEditada.getLstBienesInmuebles();

            if(lstBI == null)
                lstBI =  new ArrayList();

            lstBI.add(bij);
            fincaEditada.setLstBienesInmuebles(lstBI);
            
            HideableNode nodo = panelExpedientes.addObject((HideableNode)arbol.getSelectionPath().getLastPathComponent(),bij,true);

            Iterator itTitulares = bij.getLstTitulares().iterator();
			while (itTitulares.hasNext())
			{
				Persona p = (Persona)itTitulares.next();
				p.setBienInmueble(bij.getBienInmueble());
				HideableNode nodoPersona = new HideableNode(p);
				nodo.add(nodoPersona);                    
			}
			
			BienInmuebleCatastro bienInCat = bij.getBienInmueble();
			HideableNode nodoBienInmuebleCat = new HideableNode(bienInCat);
						
			Iterator itConstrucciones = bij.getBienInmueble().getLstConstrucciones().iterator();
			while (itConstrucciones.hasNext())
			{
				ConstruccionCatastro construccion = (ConstruccionCatastro)itConstrucciones.next();
			
				HideableNode nodoConstruccion = new HideableNode(construccion);
				nodo.add(nodoConstruccion); 
				
			}
			
			Iterator itCultivos = bij.getBienInmueble().getLstCultivos().iterator();
			while (itCultivos.hasNext())
			{
				Cultivo cultivo = (Cultivo)itCultivos.next();
				HideableNode nodoCultivo = new HideableNode(cultivo);
				
				nodo.add(nodoCultivo);
			}
            
            
            actualizaArbol(nodo);
        }
        else
        {
            HideableNode nodo = panelExpedientes.addObject((DefaultMutableTreeNode)arbol.getModel().getRoot(),bij,true);
            panelExpedientes.annadeFincaOBI(bij);
            actualizaArbol(nodo);
        }
    }
    
    public void annadeUnidadConstructivaCatastro(UnidadConstructivaCatastro ucc) {
    	if(fincaEditada!=null){
    		ArrayList lstUcc = fincaEditada.getLstUnidadesConstructivas();
    		
    		if (lstUcc == null){
    			lstUcc = new ArrayList();
    		}
    		
    		lstUcc.add(ucc);
    		fincaEditada.setLstUnidadesConstructivas(lstUcc);
    		HideableNode nodo = panelExpedientes.addObject((HideableNode)arbol.getSelectionPath().getLastPathComponent(),ucc,true);
            actualizaArbol(nodo);
    		
    		
    	}
    	
    }
    
    
    private void annadeOActualizaRepresentante(Titular representante)
    {
        bienInmuebleJuridico.getBienInmueble().setRepresentante(representante);
    }

    public void annadeConstruccion(ConstruccionCatastro cons, boolean comun)
    {
        ArrayList lstCons = fincaEditada.getLstConstrucciones();
        if(lstCons==null)
        {
            lstCons = new ArrayList();
            fincaEditada.setLstConstrucciones(lstCons);
        }
        lstCons.add(cons);
        if(!comun)
        {
            ArrayList lstConsBI = bienInmuebleJuridico.getBienInmueble().getLstConstrucciones();
            if(lstConsBI==null)
            {
                lstConsBI = new ArrayList();
                bienInmuebleJuridico.getBienInmueble().setLstConstrucciones(lstConsBI);
            }
            lstConsBI.add(cons);
        }
        HideableNode nodo = panelExpedientes.addObject((HideableNode)arbol.getSelectionPath().getLastPathComponent(),cons,true);
        actualizaArbol(nodo);
    }

    private void eliminaConstruccion()
    {
        boolean continua = true;
        ArrayList lstContrucciones = bienInmuebleJuridico.getBienInmueble().getLstConstrucciones();
        if(lstContrucciones!=null)
        {
            for (int i=0; i<lstContrucciones.size() && continua;i++)
            {
                ConstruccionCatastro cons = (ConstruccionCatastro)lstContrucciones.get(i);
                if (cons.getNumOrdenConstruccion().equalsIgnoreCase(construccion.getNumOrdenConstruccion()))
                {
                    lstContrucciones.remove(i);
                    continua = false;
                }
            }
        }
        continua = true;
        lstContrucciones = fincaEditada.getLstConstrucciones();
        if(lstContrucciones!=null)
        {
            for (int i=0; i<lstContrucciones.size() && continua;i++)
            {
                ConstruccionCatastro cons = (ConstruccionCatastro)lstContrucciones.get(i);
                if (cons.getNumOrdenConstruccion().equalsIgnoreCase(construccion.getNumOrdenConstruccion()))
                {
                    lstContrucciones.remove(i);
                    continua=false;
                }
            }
        }
        if(construccion.getDatosEconomicos().getCodModalidadReparto()!=null && construccion.getDatosEconomicos()
                .getCodModalidadReparto().length()>=2)
        {
            eliminaReparto(construccion);
        }
        TreePath path = arbol.getSelectionPath().getParentPath();
        panelExpedientes.removeObject(arbol.getSelectionPath().getLastPathComponent());
        arbol.setSelectionPath(path);
    }

    private void annadeReparto(RepartoCatastro reparto, String numOrdenConstr, String codigoSubParcela)
    {
        ArrayList lstRep = fincaEditada.getLstReparto();
        if(lstRep==null)
        {
            lstRep = new ArrayList();
            fincaEditada.setLstReparto(lstRep);
        }
        boolean existe = false;
        for(int i =0; i<lstRep.size() &&!existe &&(numOrdenConstr!=null||codigoSubParcela!=null);i++)
        {
            if(numOrdenConstr!=null)
            {
                if(((RepartoCatastro)lstRep.get(i)).getNumOrdenConsRepartir()!=null&&
                        ((RepartoCatastro)lstRep.get(i)).getNumOrdenConsRepartir().equalsIgnoreCase(numOrdenConstr))
                {
                    lstRep.set(i,reparto);
                    existe = true;
                }
            }
            if(codigoSubParcela!=null)
            {
                if(((RepartoCatastro)lstRep.get(i)).getCodSubparcelaElementoRepartir()!=null &&
                        ((RepartoCatastro)lstRep.get(i)).getCodSubparcelaElementoRepartir().equalsIgnoreCase(codigoSubParcela))
                {
                    lstRep.set(i,reparto);
                    existe = true;
                }
            }
        }
        if(!existe)
        {
            lstRep.add(reparto);
        }
    }
    
    private BienInmuebleCatastro obtenerBien(String idBienInmueble)
    {
    	BienInmuebleCatastro bien = null;
    	
        ArrayList lstBienes = fincaEditada.getLstBienesInmuebles();
        
        if(lstBienes!=null)
        {
            for (Iterator iterLstBienes = lstBienes.iterator();iterLstBienes.hasNext();){
            	
            	Object object = iterLstBienes.next();
            	if (object instanceof BienInmuebleJuridico){
            		bien = ((BienInmuebleJuridico)object).getBienInmueble();
            	}
            	else if (object instanceof BienInmuebleCatastro){
            		bien = (BienInmuebleCatastro) object;
            	}
            	else{
            		return null;
            	}
            	
            	String refFinca = GeopistaUtil.completarConCeros(bien.getIdBienInmueble().getParcelaCatastral().getRefCatastral(),14);            	
            	String numBien = GeopistaUtil.completarConCeros(bien.getIdBienInmueble().getNumCargo(),4);
            	String id = refFinca + numBien;
            	if (id.equals(idBienInmueble)){
            		return bien;
            	}
            }
        }
        return null;
    }
    
    private ConstruccionCatastro obtenerConstruccion(String idConstruccion)
    {    	
    	ConstruccionCatastro construccion = null;
    	
        ArrayList lstConstrucciones = fincaEditada.getLstConstrucciones();
        
        if(lstConstrucciones!=null)
        {
            for (Iterator iterLstBienes = lstConstrucciones.iterator();iterLstBienes.hasNext();){
            	
            	Object object = iterLstBienes.next();
            	if (object instanceof ConstruccionCatastro){
            		construccion = (ConstruccionCatastro) object;
            	}
            	else{
            		return null;
            	}
            	
            	String idCons = construccion.getIdConstruccion();            	
//            	String numBien = GeopistaUtil.completarConCeros(construccion.getIdBienInmueble().getNumCargo(),4);
//            	String id = refFinca + numBien;
            	if (idCons.equals(idConstruccion)){
            		return construccion;
            	}
            }
        }
        return null;
    }

    private void eliminaReparto(Object elementoRepartido)
    {
        ArrayList lstRepartos = fincaEditada.getLstReparto();
        ArrayList lstRepAux = new ArrayList();
        if(lstRepartos!=null)
        {
            Iterator it= lstRepartos.iterator();
            while(it.hasNext())
            {
                RepartoCatastro reparto = (RepartoCatastro)it.next();
                if((!(elementoRepartido instanceof Cultivo && reparto.getCodSubparcelaElementoRepartir()!=null &&
                        reparto.getCodSubparcelaElementoRepartir().equalsIgnoreCase(((Cultivo)elementoRepartido)
                                .getCodSubparcela())))&&
                        (!(elementoRepartido instanceof ConstruccionCatastro && reparto.getNumOrdenConsRepartir()!=null
                        && reparto.getNumOrdenConsRepartir().equalsIgnoreCase(((ConstruccionCatastro)elementoRepartido)
                        .getNumOrdenConstruccion()))))
                {
                    lstRepAux.add(reparto);
                }
            }
            fincaEditada.setLstReparto(lstRepAux);
        }
    }

    public void annadeCultivo(Cultivo cult, boolean comun)
    {
        ArrayList lstCultivos  = fincaEditada.getLstCultivos();
        if ((lstCultivos ==  null))
        {
            lstCultivos = new ArrayList();
            fincaEditada.setLstCultivos(lstCultivos);
        }
        lstCultivos.add(cult);
        if(!comun)
        {
            ArrayList lstCultBI = bienInmuebleJuridico.getBienInmueble().getLstCultivos();
            if(lstCultBI==null)
            {
                lstCultBI = new ArrayList();
                bienInmuebleJuridico.getBienInmueble().setLstCultivos(lstCultBI);
            }
            lstCultBI.add(cult);
        }
        HideableNode nodo = panelExpedientes.addObject((HideableNode)arbol.getSelectionPath().getLastPathComponent(),cult,true);
        actualizaArbol(nodo);
    }

    private void eliminaCultivo()
    {

        boolean continua = true;
        ArrayList lstCultivo = bienInmuebleJuridico.getBienInmueble().getLstCultivos();
        if(lstCultivo!=null)
        {
            for (int i=0; i<lstCultivo.size() && continua;i++)
            {
                Cultivo cult = (Cultivo)lstCultivo.get(i);
                if (cult.getCodSubparcela().equalsIgnoreCase(cultivo.getCodSubparcela()))
                {
                    lstCultivo.remove(i);
                    continua = false;
                }
            }
        }
        continua = true;
        lstCultivo = fincaEditada.getLstCultivos();
        if(lstCultivo!=null)
        {
            for (int i=0; i<lstCultivo.size() && continua;i++)
            {
                Cultivo cult = (Cultivo)lstCultivo.get(i);
                if (cult.getCodSubparcela().equalsIgnoreCase(cultivo.getCodSubparcela()))
                {
                    lstCultivo.remove(i);
                    continua=false;
                }
            }
        }
        if(cultivo.getCodModalidadReparto()!=null && cultivo.getCodModalidadReparto().length()>=2)
        {
            eliminaReparto(cultivo);
        }
        TreePath path = arbol.getSelectionPath().getParentPath();
        panelExpedientes.removeObject(arbol.getSelectionPath().getLastPathComponent());
        arbol.setSelectionPath(path);
    }

    public void annadeTitular(Titular personaTitular)
    {
        if(bienInmuebleJuridico!=null)
        {
            ArrayList lstTitulares = bienInmuebleJuridico.getLstTitulares();
            if(lstTitulares==null)
            {
                lstTitulares = new ArrayList();
                bienInmuebleJuridico.setLstTitulares(lstTitulares);
            }
            /*for(int i =0;i<lstTitulares.size();i++)
            {
                Titular tit = (Titular)lstTitulares.get(i);
                if(tit.getNif().equalsIgnoreCase(personaTitular.getNif()))
                {
                    JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Catastro.Intercambio.Edicion.Dialogs.FincaPanel.ErrorNuevoTitularDuplicado"));
                    return;
                }
            }*/
            lstTitulares.add(personaTitular);
            HideableNode nodo = panelExpedientes.addObject((HideableNode)arbol.getSelectionPath().getLastPathComponent()
                    ,personaTitular,true);
            actualizaArbol(nodo);
        }
    }

    private void eliminaTitular()
    {
        ArrayList lstTit = bienInmuebleJuridico.getLstTitulares();
        for(int i = 0; i<lstTit.size();i++)
        {
            if(((Titular)lstTit.get(i)).getNif().equalsIgnoreCase(titular.getNif()))
            {
                lstTit.remove(i);
            }
        }
        TreePath path = arbol.getSelectionPath().getParentPath();
        panelExpedientes.removeObject(arbol.getSelectionPath().getLastPathComponent());
        arbol.setSelectionPath(path);        
    }

    
    public void annadeSueloCatastro(SueloCatastro sueloCatastro)
    {
    	if(fincaEditada!=null){
    		ArrayList lstSuelo = fincaEditada.getLstSuelos();
    	
    		if (lstSuelo == null){
    			lstSuelo = new ArrayList();
    		}
    		
    		lstSuelo.add(sueloCatastro);
    		fincaEditada.setLstUnidadesConstructivas(lstSuelo);
    		HideableNode nodo = panelExpedientes.addObject((HideableNode)arbol.getSelectionPath().getLastPathComponent(),sueloCatastro,true);
            actualizaArbol(nodo);
    	}
    	
    }
    
    
    
    
    private void annadirComunidadBienes(Titular cm)
    {
        ArrayList lstComunidadBienes = new ArrayList();
        if (bienInmuebleJuridico != null)            
        {
            if(bienInmuebleJuridico.getLstComBienes() != null)
                lstComunidadBienes = bienInmuebleJuridico.getLstComBienes();

            for (int j=0;j<lstComunidadBienes.size();j++)
            {
                ComunidadBienes comunidadBien = (ComunidadBienes) lstComunidadBienes.get(j);

                if (comunidadBien.getNif()!=null &&comunidadBien.getNif().equalsIgnoreCase(cm.getNif()))
                {
                    //Se modifica la comunidad de bienes.
                    comunidadBien.setDomicilio(cm.getDomicilio());
                    comunidadBien.setRazonSocial(cm.getRazonSocial());
                    comunidadBien.setAusenciaNIF(cm.getAusenciaNIF());
                    comunidadBien.setBienInmueble(cm.getBienInmueble());
                    comunidadBien.setCodEntidadMenor(cm.getCodEntidadMenor());
                    comunidadBien.setComplementoTitularidad(cm.getComplementoTitularidad());
                    comunidadBien.setDomicilio(cm.getDomicilio());
                    comunidadBien.setExpediente(cm.getExpediente());
                    comunidadBien.setFechaAlteracion(cm.getFechaAlteracion());

                    lstComunidadBienes.remove(j);
                    lstComunidadBienes.add(j, comunidadBien);
                    loadData(comunidadBien);
                    return;
                }
            }
            ComunidadBienes com = new ComunidadBienes(cm.getNif());
            com.setAusenciaNIF(cm.getAusenciaNIF());
            com.setBienInmueble(cm.getBienInmueble());
            com.setCodEntidadMenor(cm.getCodEntidadMenor());
            com.setComplementoTitularidad(cm.getComplementoTitularidad());
            com.setDomicilio(cm.getDomicilio());
            com.setExpediente(cm.getExpediente());
            com.setFechaAlteracion(cm.getFechaAlteracion());
            com.setRazonSocial(cm.getRazonSocial());
            lstComunidadBienes.add(com);
            blackboard.put("ComBienes", false);
            loadData(com);
        }
    }

    public void limpiaTablaTitular()
    {
        TableSorted tblSorted= new TableSorted(tablepersonamodel);
        tblSorted.setTableHeader(jTableTitular.getTableHeader());
        jTableTitular.setModel(tblSorted);
    }

    public void limpiaTablaLocal()
    {
        TableSorted tblSorted= new TableSorted(tablelocalmodel);
        tblSorted.setTableHeader(jTableLocal.getTableHeader());
        jTableLocal.setModel(tblSorted);
    }

    public void limpiaTablaCultivo()
    {
        TableSorted tblSorted= new TableSorted(tablecultivomodel);
        tblSorted.setTableHeader(jTableCultivo.getTableHeader());
        jTableCultivo.setModel(tblSorted);
    }

    public void limpiaTablaBI()
    {
        TableSorted tblSorted= new TableSorted(tablebimodel);
        tblSorted.setTableHeader(jTableBienInmueble.getTableHeader());
        jTableBienInmueble.setModel(tblSorted);
    }
    
    public void limpiaTablaFinca()
    {
        TableSorted tblSorted= new TableSorted(tablefincamodel);
        tblSorted.setTableHeader(jTableFincas.getTableHeader());
        jTableFincas.setModel(tblSorted);
    }

    private void muestraAviso(String msg)
    {
            JOptionPane.showMessageDialog(this, I18N.get("Expedientes", msg),"Error",
                    JOptionPane.ERROR_MESSAGE);
    }

    public void asignaFocoManual()
    {
        FocusManager.getCurrentManager().clearGlobalFocusOwner();
        FocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        jPanelDatosFinca.requestFocusInWindow();
        focoManual = true;
    }

    public boolean isFocoAutomatico()
    {
        return focoAutomatico;
    }

    public void setFocoAutomatico(boolean focoAutomatico)
    {
        this.focoAutomatico = focoAutomatico;
    }

    public void setFocoEn(String focoEn)
    {
        this.focoEn = focoEn;
    }

    public boolean datosMinimosYCorrectosBI()
    {
        return (jTextFieldCargo.getText()!=null && !jTextFieldCargo.getText().equalsIgnoreCase("")) &&
                (jTextFieldDC1.getText()!=null && !jTextFieldDC1.getText().equalsIgnoreCase("")) &&
                (jTextFieldDC2.getText()!=null && !jTextFieldDC2.getText().equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosLocal()
    {
        return (jTextFieldIdLocal.getText()!=null && !jTextFieldIdLocal.getText().equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosCultivo()
    {
        return (jTextFieldSubparcela.getText()!=null && !jTextFieldSubparcela.getText().equalsIgnoreCase(""))
                &&(jComboBoxTipoCultivo.getSelectedItem() != null && !(((EstructuraDB)jComboBoxTipoCultivo.
                getSelectedItem()).getPatron()).equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosTitular()
    {
        return (jTextFieldNif.getText()!=null && !jTextFieldNif.getText().equalsIgnoreCase(""))
                &&(jTextFieldRazonSocial.getText() != null && !jTextFieldRazonSocial.getText().equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosComunidadBienes()
    {
        ComunidadBienes comunidad =  getComunidadBienesByNif(jComboBoxComunidadBienes.getSelectedItem().toString());
        if(comunidad!=null)
        {
            return (jTextFieldComunidadBienes.getText()!=null && !jTextFieldComunidadBienes.getText().equalsIgnoreCase(""));
        }
        return true;
    }

	public void cleanup() {
		try{
			

			if (jComboBoxDestinoLocal!=null){jComboBoxDestinoLocal.removeAll();jComboBoxDestinoLocal=null;}
			if (jComboBoxAnioPonencia!=null){jComboBoxAnioPonencia.removeAll();jComboBoxAnioPonencia=null;}
			if (jComboBoxEscalera!=null){jComboBoxEscalera.removeAll();jComboBoxEscalera=null;}
			if (jComboBoxEscaleraLocal!=null){jComboBoxEscaleraLocal.removeAll();jComboBoxEscaleraLocal=null;}
			if (jComboBoxTipologiaLocal!=null){jComboBoxTipologiaLocal.removeAll();jComboBoxTipologiaLocal=null;}
			if (jComboBoxPlanta!=null){jComboBoxPlanta.removeAll();jComboBoxPlanta=null;}
			if (jComboBoxPlantaLocal!=null){jComboBoxPlantaLocal.removeAll();jComboBoxPlantaLocal=null;}
			if (jComboBoxPuertaLocal!=null){jComboBoxPuertaLocal.removeAll();jComboBoxPuertaLocal=null;}
			if (jComboBoxPoligono!=null){jComboBoxPoligono.removeAll();jComboBoxPoligono=null;}
			if (jComboBoxTipoCultivo!=null){jComboBoxTipoCultivo.removeAll();jComboBoxTipoCultivo=null;}	
			if (jComboBoxPuerta!=null){jComboBoxPuerta.removeAll();jComboBoxPuerta=null;}		
			if (jComboBoxComunidadBienes!=null){jComboBoxComunidadBienes.removeAll();jComboBoxComunidadBienes=null;}
			
			jTextFieldCodigoVia = null;
		    jTextFieldVia = null;
		    jTextFieldNumero = null;
		    jTextFieldParcela = null;
		    jTextFieldParaje = null;
		    jTextFieldSupSolar = null;
		    jTextFieldSupTotal = null;
		    jTextFieldSupSobre = null;
		    jTextFieldSupCubierta = null;
		    jTextFieldBloque = null;
		    jTextFieldNumOrdenDH = null;
		    jTextFieldCoefParticipacion = null;
		    jTextFieldRazonSocial = null;
		    jTextFieldComunidadBienes = null;
		    jTextFieldPorcentaje = null;
		    jTextFieldNifConyuge = null;
		    jTextFieldNif=null;
		    jTextFieldDC2=null;
		    jTextFieldDC1=null;
		    jTextFieldCargo=null;
		    jTextFieldPCatastral2=null;
		    jTextFieldPCatastral1=null;
		    jTextFieldRefCatastral2=null;
		    jTextFieldRefCatastral1=null;
		    jTextFieldIdLocal = null;
		    jTextFieldUnidadConstrLocal = null;
		    jTextFieldBloqueLocal = null;
		    jTextFieldSupTotalLocal = null;
		    jTextFieldSupTerrazaLocal = null;
		    jTextFieldSuperficieCultivo = null;
			jTextFieldFechaAlteracion = null;
			jTextFieldFechaAlteracionTitular = null;

			
			if (jPanelDatosFinca!=null){jPanelDatosFinca.removeAll();}			
			if (jPanelFinca!=null){jPanelFinca.removeAll();}		
			if (jPanelDatosLocal!=null){jPanelDatosLocal.removeAll();}
			if (jPanelBotoneraFinca!=null){jPanelBotoneraFinca.removeAll();}
			if (jPanelRefCatastralFinca!=null){jPanelRefCatastralFinca.removeAll();}
			if (jPanelBienInmueble!=null){jPanelBienInmueble.removeAll();}
			if (jPanelRefCatastralFinca!=null){jPanelRefCatastralFinca.removeAll();}
			if (jPanelRefCatastralBien!=null){jPanelRefCatastralBien.removeAll();}		
			if (jPanelBienInmuebleGlobal!=null){jPanelBienInmuebleGlobal.removeAll();}		
			if (jPanelDatosBienInmueble!=null){jPanelDatosBienInmueble.removeAll();}
			if (jPanelDatosTitular!=null){jPanelDatosTitular.removeAll();}
			if (jPanelTitular!=null){jPanelTitular.removeAll();}
			if (jPanelBotoneraLocal!=null){jPanelBotoneraLocal.removeAll();}
			if (jPanelBotoneraTitular!=null){jPanelBotoneraTitular.removeAll();}
			if (jPanelComunidadBienes!=null){jPanelComunidadBienes.removeAll();}
			if (jPanelBotoneraBienInmueble!=null){jPanelBotoneraBienInmueble.removeAll();}
			if (jPanelBuscarVia!=null){jPanelBuscarVia.removeAll();}
			if (jPanelCultivo!=null){jPanelCultivo.removeAll();}
			if (jPanelDatosCultivo!=null){jPanelDatosCultivo.removeAll();}
			if (jPanelBotoneraCultivo!=null){jPanelBotoneraCultivo.removeAll();}

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	private void destroyJComboBox(JComboBox comboBox)
	{
			
		FocusListener[] listListeners=comboBox.getFocusListeners();
		System.out.println("Numero de listeners:"+listListeners.length);	
		for (int i=0;i<listListeners.length;i++){
			FocusListener l=listListeners[i];
			comboBox.removeFocusListener(l);
			System.out.println("Borrando Focus Listener");			
		}
		comboBox.removeAllItems();
		comboBox.removeAll();

	}
}
