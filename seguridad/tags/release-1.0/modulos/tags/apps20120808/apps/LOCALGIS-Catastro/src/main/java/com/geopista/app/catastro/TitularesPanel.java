package com.geopista.app.catastro;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.ViasSistemaDialog;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.intercambio.edicion.utils.UbicacionListCellRenderer;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.model.beans.Municipio;
import com.geopista.app.catastro.model.beans.Provincia;
import com.geopista.app.catastro.model.beans.Titular;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.server.administradorCartografia.PermissionException;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.util.Blackboard;

public class TitularesPanel extends JPanel implements FeatureExtendedPanel {
    
	AppContext app =(AppContext) AppContext.getApplicationContext();
	private Blackboard Identificadores = app.getBlackboard();
	            
    //Selección de titular
    private JComboBox cmbTitulares = new JComboBox();
    private JComboBox jComboBoxBienes = null;
    
    //Botones 
    //private JButton btnModificar = new JButton();
    //private JButton btnNuevo = new JButton();
    //private JButton btnBorrar = new JButton();
        
    //Otras variables
    public boolean alta=false;  
        
    private JPanel jPanelDatosLocalizacion = null;
	private JLabel jLabelCodPostal = null;
	private JLabel jLabelKm = null;
	private JLabel jLabelDirNoEstructurada = null;
	private JLabel jLabelPuerta = null;
	private JLabel jLabelPlanta = null;
	private JLabel jLabelEscalera = null;
	private JLabel jLabelBloque = null;
	private JLabel jLabelLetraD = null;
	private JLabel jLabelNumeroD = null;
	private JLabel jLabelLetra = null;
	private JLabel jLabelNumero = null;
	private JLabel jLabelDirección = null;
	private JLabel jLabelTipoVia = null;
	private JLabel jLabelProvincia = null;
	private JLabel jLabelMunicipio = null;
	private JTextField jTextFieldCodPostal = null;
	private JComboBox jComboBoxMunicipio = null;
	private JComboBox jComboBoxProvincia = null;
	private JTextField jTextFieldKm = null;
	private JTextField jTextFieldDirNoEstructurada = null;
	private JTextField jTextFieldBloque = null;
	private JTextField jTextFieldLetraD = null;
    private JTextField jTextFieldNumeroD = null;
	private JTextField jTextFieldDireccion = null;
	private JButton jButtonBuscarDireccion = null;
	private JTextField jTextFieldNumero = null;
	private JTextField jTextFieldLetra = null;
	private JPanel jPanelDatosIdentificacion = null;
	private JLabel jLabelRazonSocial = null;
	private JLabel jLabelCif = null;
	private JTextField jTextFieldCif = null;
	private JTextField jTextFieldRazonSocial = null;
	private JPanel jPanelLstTitulares = null;
	private JLabel jLabelTitulares = null;
	private ComboBoxEstructuras jComboBoxTipoVia = null;
	private JPanel jPanelBotonera = null;
	private JPanel jPanelDatosBien = null;
	private JLabel jLabelRefCatastral = null;
	private JLabel jLabelNumCargo = null;
	private JLabel jLabelDC1 = null;
	private JLabel jLabelDC2 = null;
	private JTextField jTextFieldRefCatastral = null;
	private JComboBox jComboBoxNumCargo = null;
	private JTextField jTextFieldDC1 = null;
	private JTextField jTextFieldDC2 = null;
	private JPanel jPanelDatosEconomicos = null;
	private JLabel jLabelDerecho = null;
	private JLabel jLabelPorcentaje = null;
	private JLabel jLabelNifConyuge = null;
	private ComboBoxEstructuras jComboBoxDerecho = null;
	private JTextField jTextFieldPorcentaje = null;
	private JTextField jTextFieldNifConyuge = null;
	private JLabel jLabelBienes = null;
	private JLabel jLabelFechaAlteracion = null;
	private JTextField jTextFieldFechaAlteracion = null;
	private JComboBox jComboBoxEscalera = null;
	private JComboBox jComboBoxPlanta = null;
	private JComboBox jComboBoxPuerta = null;
    
    
    public TitularesPanel()
    {
        try
        {        	
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args)
    {
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        JFrame frame1 = new JFrame(aplicacion.getI18nString("titulares.panel.titulo"));
        
        TitularesPanel geopistaEditarDatos = new TitularesPanel();
        
        frame1.getContentPane().add(geopistaEditarDatos);
        frame1.setSize(800, 475);
        frame1.setVisible(true); 
        frame1.setLocation(150, 90);
        
    }
    
    
    private void jbInit() throws Exception
    {
        //GridBagConstraints gridBagConstraints4 = new GridBagConstraints(0, 5, 1, 1, 0.01, 0.01, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
        //gridBagConstraints4.anchor = GridBagConstraints.NORTH;
        //gridBagConstraints4.weighty = 2.5;
        //gridBagConstraints4.weightx = 1.5;
        //GridBagConstraints gridBagConstraints3 = new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, 
        //		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0);
        
        //GridBagConstraints gridBagConstraints2 = new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, 
        //		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0);
        
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        this.setName(app.getI18nString("titulares.panel.nombre"));
        this.setSize(new Dimension(650, 550));
        this.setLocation(5, 10);
        
        
        //btnModificar.setText(aplicacion.getI18nString("catastro.general.aplicar"));
        //btnModificar.setBounds(new Rectangle(200, 382, 80, 20));
        //btnModificar.addActionListener(new ActionListener()
        //        {
        //    public void actionPerformed(ActionEvent e)
        //    {
        //        btnModificar_actionPerformed(e);
        //    }
        //        });
        //btnModificar.setEnabled(false);
        
        //btnNuevo.setText(aplicacion.getI18nString("catastro.general.alta"));
        //btnNuevo.setBounds(new Rectangle(300, 382, 80, 20));
        //btnNuevo.addActionListener(new ActionListener()
        //        {
        //    public void actionPerformed(ActionEvent e)
        //    {
        //        btnNuevo_actionPerformed(e);
        //    }
        //        });
        //btnNuevo.setEnabled(false);    
        
        //btnBorrar.setText(aplicacion.getI18nString("catastro.general.baja"));
        //btnBorrar.setBounds(new Rectangle(400, 382, 80, 20));
        //btnBorrar.setEnabled(false);  
                
        this.setLayout(new GridBagLayout());
        this.add(getJPanelLstTitulares(),new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, 
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0));
        this.add(getJPanelDatosIdentificacion(), new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, 
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0));
        this.add(getJPanelDatosLocalizacion(), new GridBagConstraints(0, 3, 1, 1, 0.1, 0.1, 
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0));
        
        //this.add(getJPanelBotonera(), gridBagConstraints4);
        
        this.add(getJPanelDatosBien(),new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, 
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0));
        
        this.add(getJPanelDatosEconomicos(),new GridBagConstraints(0, 4, 1, 1, 0.1, 0.1, 
        		GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 1, 0, 1), 0, 0));
         
        //Inicializa los desplegables        
        if (Identificadores.get("ListaProvincias")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerProvincias();
            Identificadores.put("ListaProvincias", lst);
            EdicionUtils.cargarLista(getJComboBoxProvincia(), lst);
            if(jComboBoxProvincia.getItemCount()>0)
            	jComboBoxProvincia.setSelectedIndex(0);            
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxProvincia(), 
                    (ArrayList)Identificadores.get("ListaProvincias"));
            
            if(jComboBoxProvincia.getItemCount()>0)
            	jComboBoxProvincia.setSelectedIndex(0);
            
        }       
        
        if (Identificadores.get("ListaEscalera")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerEscalera();
            Identificadores.put("ListaEscalera", lst);
            EdicionUtils.cargarLista(getJComboBoxEscalera(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxEscalera(), 
                    (ArrayList)Identificadores.get("ListaEscalera"));
        } 
        
        if (Identificadores.get("ListaPlanta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPlanta();
            Identificadores.put("ListaPlanta", lst);
            EdicionUtils.cargarLista(getJComboBoxPlanta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPlanta(), 
                    (ArrayList)Identificadores.get("ListaPlanta"));
        } 
        
        if (Identificadores.get("ListaPuerta")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerPuerta();
            Identificadores.put("ListaPuerta", lst);
            EdicionUtils.cargarLista(getJComboBoxPuerta(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxPuerta(), 
                    (ArrayList)Identificadores.get("ListaPuerta"));
        }                   
    }   
    
    private void cmbBienes_actionPerformed(ActionEvent e)
    {  

        alta=false;
        
        //buscar los datos del titular
        CatastroActualizarPostgre Titul = new CatastroActualizarPostgre();
        if(jComboBoxBienes.getSelectedItem()==null)return;
        
        if(jComboBoxBienes.getSelectedItem().toString().equals(""))return;
        
        Identificadores.put("NumeroCargo", jComboBoxBienes.getSelectedItem().toString().substring(14,18));
        Identificadores.put("DigitoControl1", jComboBoxBienes.getSelectedItem().toString().substring(18,19));
        Identificadores.put("DigitoControl2", jComboBoxBienes.getSelectedItem().toString().substring(19,20));
        
        //Titular titular = Titul.DatosTitular(cmbTitulares.getSelectedItem().toString(),(String)Identificadores.get("ReferenciaCatastral"));
        Titular titular = Titul.DatosTitular(cmbTitulares.getSelectedItem().toString(),jComboBoxBienes.getSelectedItem().toString());
            
        try{
        	jComboBoxTipoVia.setSelectedPatron(titular.getDomicilio().getTipoVia());
        }catch(Exception ex){
        	jComboBoxTipoVia.setSelectedPatron(null);
		}
        try{
        	jTextFieldDireccion.setText(titular.getDomicilio().getNombreVia());
        }catch(Exception ex){
        	jTextFieldDireccion.setText(null);
		}
        try{
        	if (titular.getDomicilio().getSegundoNumero() == -1){
        		jTextFieldNumero.setText("");
        	}
        	else{
        		jTextFieldNumero.setText(EdicionUtils.getStringValue(titular.getDomicilio().getPrimerNumero()));
        	}
        }catch(Exception ex){
        	jTextFieldNumero.setText(null);
        }
        try{
        	jTextFieldLetra.setText(titular.getDomicilio().getPrimeraLetra());
        }catch(Exception ex){
        	jTextFieldLetra.setText(null);
        }
        try{
        	if (titular.getDomicilio().getSegundoNumero() == -1){
        		jTextFieldNumeroD.setText("");
        	}
        	else{
        		jTextFieldNumeroD.setText(EdicionUtils.getStringValue(titular.getDomicilio().getSegundoNumero()));
        	}
        }catch(Exception ex){
        	jTextFieldNumeroD.setText(null);
        }
        try{
        	jTextFieldLetraD.setText(titular.getDomicilio().getSegundaLetra());
        }catch(Exception ex){
        	jTextFieldLetraD.setText(null);
        }
        try{
        	jTextFieldBloque.setText(titular.getDomicilio().getBloque());
        }catch(Exception ex){
        	jTextFieldBloque.setText(null);
        }
       
        EstructuraDB eEscalera = new EstructuraDB();   
        try{
        	                 	
        	eEscalera.setPatron(titular.getDomicilio().getEscalera());        
        	jComboBoxEscalera.setSelectedItem(eEscalera);
        }
        catch(Exception ex){
        	if(jComboBoxEscalera.getItemCount()>0)
        		jComboBoxEscalera.setSelectedIndex(0);
        }
        EstructuraDB ePlanta = new EstructuraDB();   
        try{
        	                 	
        	ePlanta.setPatron(titular.getDomicilio().getPlanta());        
        	jComboBoxPlanta.setSelectedItem(ePlanta);
        }
        catch(Exception ex){
        	if(jComboBoxPlanta.getItemCount()>0)
        		jComboBoxPlanta.setSelectedIndex(0);
        }
        EstructuraDB ePuerta = new EstructuraDB();   
        try{
        	                 	
        	ePuerta.setPatron(titular.getDomicilio().getPuerta());        
        	jComboBoxPuerta.setSelectedItem(ePuerta);
        }
        catch(Exception ex){
        	if(jComboBoxPuerta.getItemCount()>0)
        		jComboBoxPuerta.setSelectedIndex(0);
        }
        
        try{
        	if (titular.getDomicilio().getCodigoPostal()==null){
        		jTextFieldCodPostal.setText("");
        	}
        	else{
        		jTextFieldCodPostal.setText(EdicionUtils.getStringValue(titular.getDomicilio().getCodigoPostal()));
        	}
        }catch(Exception ex){
        	jTextFieldCodPostal.setText(null);
        }
        try{
        	jTextFieldDirNoEstructurada.setText(titular.getDomicilio().getDireccionNoEstructurada());
        }catch(Exception ex){
        	jTextFieldDirNoEstructurada.setText(null);
        }
        try{
        	if (titular.getDomicilio().getKilometro() == -1){
        		jTextFieldKm.setText("");
        	}
        	else{
        		jTextFieldKm.setText(EdicionUtils.getStringValue(titular.getDomicilio().getKilometro()));
        	}
        }catch(Exception ex){
        	jTextFieldKm.setText(null);
        }
        try{
        	jTextFieldNifConyuge.setText(titular.getNifConyuge());
        }catch(Exception ex){
        	jTextFieldNifConyuge.setText(null);
        }
        try{
        	jTextFieldDC1.setText(titular.getDerecho().getIdBienInmueble().getDigControl1());
        }catch(Exception ex){
        	jTextFieldDC1.setText(null);
        }
        try{
        	jTextFieldDC2.setText(titular.getDerecho().getIdBienInmueble().getDigControl2());
        }catch(Exception ex){
        	jTextFieldDC2.setText(null);
        }
        try{
        	jComboBoxNumCargo.setSelectedItem(titular.getDerecho().getIdBienInmueble().getNumCargo());
        }catch(Exception ex){
        	jComboBoxNumCargo.setSelectedIndex(-1);
        }
        try{
        	jComboBoxDerecho.setSelectedPatron(titular.getDerecho().getCodDerecho());
        }catch(Exception ex){
        	if(jComboBoxNumCargo.getItemCount()>0)
        		jComboBoxNumCargo.setSelectedIndex(0);
        }
        try{
        	jTextFieldPorcentaje.setText(new Float(titular.getDerecho().getPorcentajeDerecho()).toString());
        }catch(Exception ex){
        	jTextFieldDC2.setText(null);
        }
        
        
        jTextFieldCif.setText(titular.getNif());
        jTextFieldRazonSocial.setText(titular.getRazonSocial());  
        
       try{ 
    	   jTextFieldFechaAlteracion.setText(titular.getFechaAlteracion());    		
    	}
    	catch(Exception ex){
    		jTextFieldFechaAlteracion.setText(null);
    	}
                
        //Datos de localización
        if (titular.getDomicilio().getProvinciaINE()!=null)
        {
            Provincia p = new Provincia();
            NumberFormat formatter1 = new DecimalFormat("00");
            p.setIdProvincia(formatter1.format(new Integer(titular.getDomicilio().getProvinciaINE()).intValue()));
            p.setNombreOficial(titular.getDomicilio().getNombreProvincia());
            jComboBoxProvincia.setSelectedItem(p);
            
            if (titular.getDomicilio().getMunicipioINE()!=null)
            {
                //TODO: DE MOMENTO SE ESTÁ COMPARANDO CON EL ID INE, PORQ NO HAY ID_CATASTRO EN BD
            	NumberFormat formatter = new DecimalFormat("000");
                Municipio m = new Municipio();
                m.setIdIne(formatter.format(new Integer(titular.getDomicilio().getMunicipioINE()).intValue()));
                m.setNombreOficial(titular.getDomicilio().getNombreMunicipio());
                m.setId(new Integer(titular.getDomicilio().getProvinciaINE()+formatter.format(new Integer(titular.getDomicilio().getMunicipioINE()).intValue())).intValue());
                m.setProvincia((Provincia)jComboBoxProvincia.getSelectedItem());
                jComboBoxMunicipio.setSelectedItem(m);
                
            }                
        }        
    }
    
    private void cmbTitulares_actionPerformed(ActionEvent e)
    {   
    
    	cleanDialog();
    	//btnBorrar.setEnabled(false);
        if(cmbTitulares.getSelectedItem()==null)return;
                
        Identificadores.put("NIF", cmbTitulares.getSelectedItem().toString());
        
        if(cmbTitulares.getSelectedIndex()!= -1){
			jComboBoxBienes.setEnabled(true);
			String refCatastral = jTextFieldRefCatastral.getText();
			String nif = cmbTitulares.getSelectedItem().toString();
	        if(refCatastral!=null || nif !=null){
	        	jComboBoxBienes.setSelectedIndex(-1);
	        	EdicionOperations oper = new EdicionOperations();
	        	ArrayList lst = oper.obtenerBienes(refCatastral,nif);
	        	EdicionUtils.cargarLista(getJComboBoxBienes(), lst);
	        	if(jComboBoxBienes.getItemCount()>0){
	        		jComboBoxBienes.setSelectedIndex(-1);
	        	}
	        	else{
	        		jComboBoxBienes.setEnabled(false);
	        	}
	        }
		}
          
    }
  
    
    public void enter()
    {
        
        Blackboard Identificadores = app.getBlackboard();
        alta = false;
               
        //busca los titulares de la referencia catastral 
        CatastroActualizarPostgre Titul = new CatastroActualizarPostgre();
        int ID_Parcela= Integer.parseInt(Identificadores.get("ID_Parcela").toString());
        
        //se recuperan por este orden: id, nif, codigoderecho, identificaciontitular
        ArrayList Datos = new ArrayList();
        try{
        	Datos= Titul.IdsTitulares(ID_Parcela);
        }
        catch (PermissionException pe)
        {
        	this.removeAll();
        	this.setLayout(new GridBagLayout());
        	JLabel label = new JLabel("No tiene permisos para visualizar el contenido de esta pestaña", JLabel.CENTER);
        	label.setFont(new Font("Arial", Font.BOLD, 12 ) );
            this.add(label,
            		new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
            				GridBagConstraints.HORIZONTAL, new Insets(30, 5, 0, 5), 0, 0));
            
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        String Nif= "";
        cmbTitulares.removeAllItems();
        
        Iterator alIt = Datos.iterator();
        while (alIt.hasNext()) 
        {
            Nif = alIt.next().toString();
            cmbTitulares.addItem(Nif);            
            
        }
                
        cmbTitulares.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		cmbTitulares_actionPerformed(e);
        		
        	}
        });
        
        jComboBoxBienes.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		cmbBienes_actionPerformed(e);
        	}
        });

        cmbTitulares.setSelectedIndex(-1);
        cleanDialog(); 
        setEditable(false);
        
        String refCatastral = (String)Identificadores.get("ReferenciaCatastral");
        if(refCatastral!=null){
        	jTextFieldRefCatastral.setText(refCatastral);
        	EdicionOperations oper1 = new EdicionOperations();
        	ArrayList lst1 = oper1.obtenerNumerosCargo(refCatastral);
        	EdicionUtils.cargarLista(getJComboBoxNumCargo(), lst1);
        	if(jComboBoxNumCargo.getItemCount()>0){
        		jComboBoxNumCargo.setSelectedIndex(-1);
        	}
        }
                       
    }
 
	public void exit()
    {
    }
    
	/*
    private void btnBorrar_actionPerformed(ActionEvent e)
    {
        AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
        
        Object options[] = {aplicacion.getI18nString("catastro.confirmacion.si"), aplicacion.getI18nString("catastro.confirmacion.no")};
        int index =
            JOptionPane.showOptionDialog(null, // parent
                    aplicacion.getI18nString("catastro.confirmacion.mensaje"), // message object
                    aplicacion.getI18nString("catastro.confirmacion.titulo"), // string title
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, // Icon
                    options,
                    options[1]
            );
        switch (index) {
        case 0:
            
            CatastroActualizarPostgre ActualizarTitular = new CatastroActualizarPostgre();
            String nif = (String)Identificadores.get("NIF");
            String refCatastral = (String)Identificadores.get("ReferenciaCatastral");
            String numCargo = (String)Identificadores.get("NumeroCargo");
            String digito1 = (String)Identificadores.get("DigitoControl1");
            String digito2 = (String)Identificadores.get("DigitoControl2");
            String Result = ActualizarTitular.BajaTitular (nif,refCatastral,numCargo,digito1,digito2);
            if (Result.equals("OK")){
                enter();
            }
            
            break;
        case 1:
            break;
            
        }
    }
    */
    
    /**
     * This method initializes jPanelDatosLocalizacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosLocalizacion()
    {
        if (jPanelDatosLocalizacion == null)
        {
            
            jPanelDatosLocalizacion = new JPanel(new GridBagLayout());
            jPanelDatosLocalizacion.setPreferredSize(new Dimension(500,200));
            jPanelDatosLocalizacion.setBorder(BorderFactory.createTitledBorder
                    (null, app.getI18nString("titulares.panel.localizacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jLabelCodPostal = new JLabel("", JLabel.CENTER); 
            jLabelCodPostal.setText(app.getI18nString("titulares.panel.localizacion.codpostal"));            
            jLabelKm = new JLabel("", JLabel.CENTER); 
            jLabelKm.setText(app.getI18nString("titulares.panel.localizacion.km"));            
            jLabelDirNoEstructurada = new JLabel("", JLabel.CENTER); 
            jLabelDirNoEstructurada.setText(app.getI18nString("titulares.panel.localizacion.dirnoestructurada")); 
            jLabelPuerta = new JLabel("", JLabel.CENTER); 
            jLabelPuerta.setText(app.getI18nString("titulares.panel.localizacion.puerta")); 
            jLabelPlanta  = new JLabel("", JLabel.CENTER); 
            jLabelPlanta.setText(app.getI18nString("titulares.panel.localizacion.planta")); 
            jLabelEscalera = new JLabel("", JLabel.CENTER); 
            jLabelEscalera.setText(app.getI18nString("titulares.panel.localizacion.escalera")); 
            jLabelBloque = new JLabel("", JLabel.CENTER); 
            jLabelBloque.setText(app.getI18nString("titulares.panel.localizacion.bloque")); 
            jLabelLetraD = new JLabel("", JLabel.CENTER); 
            jLabelLetraD.setText(app.getI18nString("titulares.panel.localizacion.letraD")); 
            jLabelNumeroD = new JLabel("", JLabel.CENTER); 
            jLabelNumeroD.setText(app.getI18nString("titulares.panel.localizacion.numeroD")); 
            jLabelLetra = new JLabel("", JLabel.CENTER); 
            jLabelLetra.setText(app.getI18nString("titulares.panel.localizacion.letra")); 
            jLabelNumero = new JLabel("", JLabel.CENTER); 
            jLabelNumero.setText(app.getI18nString("titulares.panel.localizacion.numero")); 
            jLabelDirección = new JLabel("", JLabel.CENTER); 
            jLabelDirección.setText(app.getI18nString("titulares.panel.localizacion.direccion")); 
            jLabelTipoVia = new JLabel("", JLabel.CENTER); 
            jLabelTipoVia.setText(app.getI18nString("titulares.panel.localizacion.tipovia")); 
            jLabelProvincia  = new JLabel("", JLabel.CENTER); 
            jLabelProvincia.setText(app.getI18nString("titulares.panel.localizacion.provincia")); 
            jLabelMunicipio = new JLabel("", JLabel.CENTER); 
            jLabelMunicipio.setText(app.getI18nString("titulares.panel.localizacion.municipio")); 
            
            jPanelDatosLocalizacion.add(jLabelProvincia, 
                    new GridBagConstraints(0, 0, 4, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));            
            jPanelDatosLocalizacion.add(jLabelMunicipio, 
                    new GridBagConstraints(4, 0, 6, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelTipoVia, 
                    new GridBagConstraints(0, 2, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelDirección, 
                    new GridBagConstraints(2, 2, 7, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJComboBoxTipoVia(), 
                    new GridBagConstraints(0, 3, 2, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldDireccion(), 
                    new GridBagConstraints(2, 3, 7, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJButtonBuscarDireccion(), 
                    new GridBagConstraints(9, 3, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelNumero, 
                    new GridBagConstraints(0, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelLetra, 
                    new GridBagConstraints(1, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelNumeroD, 
                    new GridBagConstraints(2, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelLetraD, 
                    new GridBagConstraints(3, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelBloque, 
                    new GridBagConstraints(4, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelEscalera, 
                    new GridBagConstraints(5, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelPlanta, 
                    new GridBagConstraints(6, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelPuerta, 
                    new GridBagConstraints(7, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldNumero(), 
                    new GridBagConstraints(0, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldLetra(), 
                    new GridBagConstraints(1, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldNumeroD(), 
                    new GridBagConstraints(2, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldLetraD(), 
                    new GridBagConstraints(3, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldBloque(), 
                    new GridBagConstraints(4, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
           
            jPanelDatosLocalizacion.add(getJComboBoxEscalera(), 
                    new GridBagConstraints(5, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosLocalizacion.add(getJComboBoxPlanta(), 
                    new GridBagConstraints(6, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosLocalizacion.add(getJComboBoxPuerta(), 
                    new GridBagConstraints(7, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelDirNoEstructurada, 
                    new GridBagConstraints(0, 7, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelKm, 
                    new GridBagConstraints(9, 7, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldDirNoEstructurada(), 
                    new GridBagConstraints(0, 8, 8, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 5, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldKm(), 
                    new GridBagConstraints(9, 8, 7, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJComboBoxProvincia(), 
                    new GridBagConstraints(0, 1, 4, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJComboBoxMunicipio(), 
                    new GridBagConstraints(4, 1, 6, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(jLabelCodPostal, 
                    new GridBagConstraints(9, 5, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosLocalizacion.add(getJTextFieldCodPostal(), 
                    new GridBagConstraints(9, 6, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
        }
        return jPanelDatosLocalizacion;
    }
    
    /**
     * This method initializes jTextFieldDireccion	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDireccion()
    {
        if (jTextFieldDireccion == null)
        {
            jTextFieldDireccion = new TextField(25);
        }
        return jTextFieldDireccion;
    }
    
    /**
     * This method initializes jButtonBuscarDireccion	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonBuscarDireccion()
    {
        if (jButtonBuscarDireccion == null)
        {
            jButtonBuscarDireccion = new JButton();
            jButtonBuscarDireccion.setIcon(IconLoader.icon(GestionExpedientePanel.ICONO_BUSCAR));
            jButtonBuscarDireccion.addActionListener(new java.awt.event.ActionListener()
                    {
                public void actionPerformed(java.awt.event.ActionEvent e)
                {
                    ViasSistemaDialog dialog = new ViasSistemaDialog(jTextFieldDireccion.getText());
                    dialog.setVisible(true); 
                    
                    String nombreVia = dialog.getVia();                    
                    jTextFieldDireccion.setText(nombreVia);
                }
                    });
            jButtonBuscarDireccion.setName("_buscarvia");
        }
        return jButtonBuscarDireccion;
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
            jTextFieldNumero = new TextField(4);
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
     * This method initializes jTextFieldLetra	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLetra()
    {
        if (jTextFieldLetra == null)
        {
            jTextFieldLetra = new TextField(1);
        }
        return jTextFieldLetra;
    }
    
    /**
     * This method initializes jTextFieldNumeroD	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldNumeroD()
    {
        if (jTextFieldNumeroD == null)
        {
            jTextFieldNumeroD = new TextField(4);
            jTextFieldNumeroD.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldNumeroD,4, app.getMainFrame());
                }
                    });
        }
        return jTextFieldNumeroD;
    }
    
    /**
     * This method initializes jTextFieldLetraD	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldLetraD()
    {
        if (jTextFieldLetraD == null)
        {
            jTextFieldLetraD = new TextField(1);
        }
        return jTextFieldLetraD;
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
        if (jComboBoxPlanta  == null)
        {
        	jComboBoxPlanta = new JComboBox();
        	jComboBoxPlanta.setRenderer(new EstructuraDBListCellRenderer());
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
        if (jComboBoxPuerta  == null)
        {
        	jComboBoxPuerta = new JComboBox();
        	jComboBoxPuerta.setRenderer(new EstructuraDBListCellRenderer());
        }
        return jComboBoxPuerta;
    }
    
    /**
     * This method initializes jTextFieldDirNoEstructurada	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldDirNoEstructurada()
    {
        if (jTextFieldDirNoEstructurada == null)
        {
            jTextFieldDirNoEstructurada = new TextField(25);
        }
        return jTextFieldDirNoEstructurada;
    }
    
    /**
     * This method initializes jTextFieldKm	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldKm()
    {
        if (jTextFieldKm == null)
        {
            jTextFieldKm = new TextField(6);
            jTextFieldKm.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldKm,6, app.getMainFrame());
                }
                    });
        }
        return jTextFieldKm;
    }
    
    
    /**
     * This method initializes jComboBoxProvincia	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxProvincia()
    {
        if (jComboBoxProvincia == null)
        {
            jComboBoxProvincia = new JComboBox();
            jComboBoxProvincia.setRenderer(new UbicacionListCellRenderer());
            
            jComboBoxProvincia.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {   
                    
                    if (jComboBoxProvincia.getSelectedIndex()==0)
                    {
                        jComboBoxMunicipio.removeAllItems();
                    }
                    else
                    {
                    	if(jComboBoxProvincia.getSelectedItem()!=null){
                    		EdicionOperations oper = new EdicionOperations();
                    		EdicionUtils.cargarLista(getJComboBoxMunicipio(), 
                    				oper.obtenerMunicipios((Provincia)jComboBoxProvincia.getSelectedItem()));
                    	}
                    }
                    
                    
                }
                    });
        }
        return jComboBoxProvincia;
    }
    
    /**
     * This method initializes jComboBoxMunicipio	
     * 	
     * @return javax.swing.JComboBox	
     */
    private JComboBox getJComboBoxMunicipio()
    {
        if (jComboBoxMunicipio == null)
        {
            jComboBoxMunicipio = new JComboBox();
            jComboBoxMunicipio.setRenderer(new UbicacionListCellRenderer());
        }
        return jComboBoxMunicipio;
    }
    
    /**
     * This method initializes jTextFieldCodPostal	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCodPostal()
    {
        if (jTextFieldCodPostal == null)
        {
            jTextFieldCodPostal = new TextField(5);
            jTextFieldCodPostal.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldCodPostal,5, app.getMainFrame());
                }
                    });
        }
        return jTextFieldCodPostal;
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosIdentificacion()
    {
        if (jPanelDatosIdentificacion == null)
        {           	
            jLabelRazonSocial  = new JLabel("", JLabel.CENTER); 
            jLabelRazonSocial.setText(app.getI18nString("titulares.panel.identificacion.razonsocial")); 
            
            jLabelCif  = new JLabel("", JLabel.CENTER); 
            jLabelCif.setText(app.getI18nString("titulares.panel.identificacion.nifcif")); 
            
            jLabelFechaAlteracion   = new JLabel("", JLabel.CENTER); 
            jLabelFechaAlteracion.setText(app.getI18nString("titulares.panel.identificacion.fechaalteracion"));
            
            jPanelDatosIdentificacion = new JPanel(new GridBagLayout());
            jPanelDatosIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, app.getI18nString("titulares.panel.identificacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
            
            jPanelDatosIdentificacion.add(jLabelCif, 
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelRazonSocial,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(jLabelFechaAlteracion,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosIdentificacion.add(getJTextFieldCif(), 
            		new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
            				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJTextFieldRazonSocial(), 
            		new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
            				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosIdentificacion.add(getJTextFieldFechaAlteracion(), 
            		new GridBagConstraints(2, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
            				GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosIdentificacion.setPreferredSize(new Dimension(500,80));
            
        }
        return jPanelDatosIdentificacion;
    }
    
    /**
     * This method initializes jTextFieldCif	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldCif()
    {
        if (jTextFieldCif == null)
        {
            jTextFieldCif = new TextField(9);
        }
        return jTextFieldCif;
    }
    
    /**
     * This method initializes jTextFieldFechaAlteracion    
     *  
     * @return javax.swing.JTextField   
     */
    private JTextField getJTextFieldFechaAlteracion()
    {
        if (jTextFieldFechaAlteracion   == null)
        {
        	jTextFieldFechaAlteracion = new TextField(10);        	
        }
        return jTextFieldFechaAlteracion;
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
    
    private JPanel getJPanelLstTitulares()
    {
        if (jPanelLstTitulares  == null)
        {               
            GridBagConstraints gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints(1, 1, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            
            jLabelTitulares   = new JLabel("", JLabel.CENTER); 
            jLabelTitulares.setText(app.getI18nString("titulares.panel.listatitulares.seleccion"));
            
            jLabelBienes = new JLabel("", JLabel.CENTER); 
            jLabelBienes.setText(app.getI18nString("titulares.panel.listatitulares.seleccionbienes"));
                        
            jPanelLstTitulares = new JPanel(new GridBagLayout());
            jPanelLstTitulares.setBorder(BorderFactory.createTitledBorder
                    (null, app.getI18nString("titulares.panel.listatitulares.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelLstTitulares.add(jLabelTitulares,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelLstTitulares.add(jLabelBienes,
                    new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelLstTitulares.add(cmbTitulares, gridBagConstraints);
            jPanelLstTitulares.add(getJComboBoxBienes(), gridBagConstraints1);
                        
            jPanelLstTitulares.setPreferredSize(new Dimension(500,80));
            
        }
        return jPanelLstTitulares;
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
            Estructuras.cargarEstructura("Tipo de vía");
            jComboBoxTipoVia = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxTipoVia;
    }
    
    /*
    private JPanel getJPanelBotonera()
    {
        if (jPanelBotonera   == null)
        {               
            GridBagConstraints gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            
            jPanelBotonera = new JPanel(new GridBagLayout());
           
            jPanelBotonera.add(btnNuevo,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelBotonera.add(btnModificar,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelBotonera.add(btnBorrar,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
                        
            jPanelBotonera.setPreferredSize(new Dimension(500,50));
            
        }
        return jPanelBotonera;
    }
    */
    
    private void cleanDialog(){
    	if(jComboBoxTipoVia.getItemCount()>0)
    		jComboBoxTipoVia.setSelectedIndex(0);       
    	if(jComboBoxProvincia.getItemCount()>0)
    		jComboBoxProvincia.setSelectedIndex(0);
    	Municipio m = new Municipio();
    	jComboBoxMunicipio.addItem(m);
    	jComboBoxMunicipio.setSelectedItem(m);
    	jComboBoxMunicipio.removeAllItems();
    	jTextFieldCif.setText("");
    	jTextFieldRazonSocial.setText("");
    	jTextFieldFechaAlteracion.setText("");
    	jTextFieldDireccion.setText("");
    	jTextFieldNumero.setText("");
    	jTextFieldLetra.setText("");
    	jTextFieldNumeroD.setText("");
    	jTextFieldLetraD.setText("");
    	jTextFieldBloque.setText("");
    	if(jComboBoxEscalera.getItemCount()>0)
    		jComboBoxEscalera.setSelectedIndex(0);
    	if(jComboBoxPlanta.getItemCount()>0)
    		jComboBoxPlanta.setSelectedIndex(0);
    	if(jComboBoxPuerta.getItemCount()>0)
    		jComboBoxPuerta.setSelectedIndex(0);
    	jTextFieldCodPostal.setText("");
    	jTextFieldDirNoEstructurada.setText("");
    	jTextFieldKm.setText("");
    	if(jComboBoxDerecho.getItemCount()>0)
    		jComboBoxDerecho.setSelectedIndex(0);
    	jTextFieldPorcentaje.setText("");
    	jTextFieldNifConyuge.setText("");
    	jTextFieldDC1.setText("");
    	jTextFieldDC2.setText("");
    	jComboBoxNumCargo.setSelectedIndex(-1);
    	if(jComboBoxBienes.isEnabled() && jComboBoxBienes.getItemCount()>0){      		
    		jComboBoxBienes.setSelectedIndex(0);
    	}
    	jComboBoxBienes.removeAllItems();
    	
    }
    
    private void setEditable(boolean editable){

    	getJComboBoxTipoVia().setEnabled(editable);     
    	getJComboBoxProvincia().setEnabled(editable); 
    	getJComboBoxMunicipio().setEnabled(editable); 
    	getJTextFieldCif().setEditable(editable); 
    	getJTextFieldRazonSocial().setEditable(editable); 
    	getJTextFieldFechaAlteracion().setEditable(editable); 
    	getJTextFieldDireccion().setEditable(editable); 
    	getJTextFieldNumero().setEditable(editable); 
    	getJTextFieldLetra().setEditable(editable); 
    	getJTextFieldNumeroD().setEditable(editable); ;
    	getJTextFieldLetraD().setEditable(editable); 
    	getJTextFieldBloque().setEditable(editable); 
    	getJComboBoxEscalera().setEnabled(editable); 
    	getJComboBoxPlanta().setEnabled(editable); 
    	getJComboBoxPuerta().setEnabled(editable); 
    	getJTextFieldCodPostal().setEditable(editable); 
    	getJTextFieldDirNoEstructurada().setEditable(editable); 
    	getJTextFieldKm().setEditable(editable); 
    	getJComboBoxDerecho().setEnabled(editable); 
    	getJTextFieldPorcentaje().setEditable(editable); 
    	getJTextFieldNifConyuge().setEditable(editable); 
    	getJTextFieldDC1().setEditable(editable); 
    	getJTextFieldDC2().setEditable(editable); 
    	getJComboBoxNumCargo().setEnabled(editable); 
    	getJComboBoxBienes().setEnabled(editable); 
    	getJTextFieldRefCatastral().setEditable(editable);
    	
    }
    
    private JPanel getJPanelDatosBien()
    {
        if (jPanelDatosBien   == null)
        {               
            GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            
            jLabelRefCatastral = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral.setText(app.getI18nString("titulares.panel.datosbien.refcatastral"));
                        
            jLabelNumCargo = new JLabel("", JLabel.CENTER); 
            jLabelNumCargo.setText(app.getI18nString("titulares.panel.datosbien.numcargo"));
            
            jLabelDC1 = new JLabel("", JLabel.CENTER); 
            jLabelDC1.setText(app.getI18nString("titulares.panel.datosbien.dc1"));
                        
            jLabelDC2 = new JLabel("", JLabel.CENTER); 
            jLabelDC2.setText(app.getI18nString("titulares.panel.datosbien.dc2"));
                        
            jPanelDatosBien = new JPanel(new GridBagLayout());
            jPanelDatosBien.setBorder(BorderFactory.createTitledBorder
                    (null, app.getI18nString("titulares.panel.datosbien.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosBien.add(jLabelRefCatastral,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosBien.add(jLabelNumCargo,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosBien.add(jLabelDC1,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosBien.add(jLabelDC2,
                    new GridBagConstraints(3, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            
            jPanelDatosBien.add(getJTextFieldRefCatastral(), gridBagConstraints);
            jPanelDatosBien.add(getJComboBoxNumCargo(), gridBagConstraints1);
            jPanelDatosBien.add(getJTextFieldDC1(), gridBagConstraints2);
            jPanelDatosBien.add(getJTextFieldDC2(), gridBagConstraints3);
                        
            jPanelDatosBien.setPreferredSize(new Dimension(500,80));
            
        }
        return jPanelDatosBien;
    }
    
    private JTextField getJTextFieldRefCatastral()
    {
        if (jTextFieldRefCatastral  == null)
        {
        	jTextFieldRefCatastral = new TextField(15);
        	jTextFieldRefCatastral.setEditable(false);
        }
        return jTextFieldRefCatastral;
    }
    
    private JComboBox getJComboBoxNumCargo()
    {
        if (jComboBoxNumCargo  == null)
        {
        	jComboBoxNumCargo = new JComboBox();
        	jComboBoxNumCargo.addActionListener(new ActionListener()
                    {
                public void actionPerformed(ActionEvent e)
                {   
                    String DC1 = "";
                    String DC2 = "";
                	
                    if (jComboBoxNumCargo.getSelectedIndex()==-1)
                    {
                        jTextFieldDC1.setText("");
                        jTextFieldDC2.setText("");
                    }
                    else
                    {
                    	if(jComboBoxNumCargo.getSelectedItem()!=null){
                    		
                    		String refCatastral = (String)Identificadores.get("ReferenciaCatastral");
                    		CatastroActualizarPostgre CargarDigito = new CatastroActualizarPostgre();
                    		DC1 = CargarDigito.cargarDigitoControl1(refCatastral, jComboBoxNumCargo.getSelectedItem().toString());
                    		if(DC1!=null){
                    			jTextFieldDC1.setText(DC1);
                    		}
                    		else{
                    			jTextFieldDC1.setText("");
                    		}
                    		DC2 = CargarDigito.cargarDigitoControl2(refCatastral, jComboBoxNumCargo.getSelectedItem().toString());
                    		if(DC2!=null){
                    			jTextFieldDC2.setText(DC2);
                    		}
                    		else{
                    			jTextFieldDC2.setText("");
                    		}
                    	}
                    }
                    
                    
                }
                    });
        }
        return jComboBoxNumCargo;
    }
    
    private JTextField getJTextFieldDC1()
    {
        if (jTextFieldDC1  == null)
        {
        	jTextFieldDC1 = new TextField(1);
        	jTextFieldDC1.setEditable(false);
        }
        return jTextFieldDC1;
    }
    
    private JTextField getJTextFieldDC2()
    {
        if (jTextFieldDC2  == null)
        {
        	jTextFieldDC2 = new TextField(1);
        	jTextFieldDC2.setEditable(false);
        }
        return jTextFieldDC2;
    }
    
    private JPanel getJPanelDatosEconomicos()
    {
        if (jPanelDatosEconomicos    == null)
        {               
            GridBagConstraints gridBagConstraints = new GridBagConstraints(0, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints(1, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints(3, 2, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
            
            jLabelDerecho = new JLabel("", JLabel.CENTER); 
            jLabelDerecho.setText(app.getI18nString("titulares.panel.datoseconomicos.derecho"));
                        
            jLabelPorcentaje = new JLabel("", JLabel.CENTER); 
            jLabelPorcentaje.setText(app.getI18nString("titulares.panel.datoseconomicos.porcentaje"));
            
            jLabelNifConyuge = new JLabel("", JLabel.CENTER); 
            jLabelNifConyuge.setText(app.getI18nString("titulares.panel.datoseconomicos.nifconyuge"));
                     
            jPanelDatosEconomicos = new JPanel(new GridBagLayout());
            jPanelDatosEconomicos.setBorder(BorderFactory.createTitledBorder
                    (null, app.getI18nString("titulares.panel.datoseconomicos.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            jPanelDatosEconomicos.add(jLabelDerecho,
                    new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelPorcentaje,
                    new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
            jPanelDatosEconomicos.add(jLabelNifConyuge,
                    new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 5, 0, 5), 0, 0));
                        
            jPanelDatosEconomicos.add(getJComboBoxDerecho(), gridBagConstraints);
            jPanelDatosEconomicos.add(getJTextFieldPorcentaje(), gridBagConstraints1);
            jPanelDatosEconomicos.add(getJTextFieldNifConyuge(), gridBagConstraints2);
            
            jPanelDatosEconomicos.setPreferredSize(new Dimension(500,80));
            
        }
        return jPanelDatosEconomicos;
    }
    
    private ComboBoxEstructuras getJComboBoxDerecho()
    {
        if (jComboBoxDerecho  == null)
        {
            Estructuras.cargarEstructura("Tipo de derecho de titularidad");
            jComboBoxDerecho = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
        }
        return jComboBoxDerecho;
    }
    
    private JTextField getJTextFieldPorcentaje()
    {
        if (jTextFieldPorcentaje == null)
        {
        	jTextFieldPorcentaje = new TextField(10);
        }
        return jTextFieldPorcentaje;
    }
    
    private JTextField getJTextFieldNifConyuge()
    {
        if (jTextFieldNifConyuge == null)
        {
        	jTextFieldNifConyuge = new TextField(9);
        }
        return jTextFieldNifConyuge;
    }
    
    private JComboBox getJComboBoxBienes()
    {
        if (jComboBoxBienes == null)
        {
        	jComboBoxBienes = new JComboBox();
        	jComboBoxBienes.setEnabled(false);
        }
        return jComboBoxBienes;
    }
    
}  