package com.geopista.app.catastro.intercambio.edicion.dialogs;

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
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.intercambio.edicion.utils.EstructuraDBListCellRenderer;
import com.geopista.app.catastro.model.beans.Cultivo;
import com.geopista.app.catastro.model.beans.EstructuraDB;
import com.geopista.app.catastro.registroExpedientes.utils.UtilRegistroExp;
import com.geopista.app.utilidades.TextField;
import com.geopista.app.utilidades.estructuras.ComboBoxEstructuras;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.util.GeopistaFunctionUtils;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;


public class CultivoExtendedInfoPanel extends JPanel
{
    private JPanel jPanelDatosIdentificacion = null;
      
    
    private JLabel jLabelCargo;
    private JLabel jLabelRefCatastral1;
    private JLabel jLabelRefCatastral2;
    
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Blackboard blackboard = app.getBlackboard();
	private JPanel jPanelDatosSubparcela = null;
	
	private JTextField jTextFieldRefCatastral1 = null;
	private JTextField jTextFieldRefCatastral2 = null;
	private JLabel jLabelDenominacion = null;
	private JLabel jLabelIntensidad = null;
	private JComboBox jComboBoxIntensidad = null;
	private JPanel jSpacePanel = null;
	private JLabel jSubparcelaLabel = null;
	private JLabel jTipoCultivoLabel = null;
	private JTextField jTextFieldSubparcela = null;
	private JComboBox jComboBoxCalifCultivo = null;
	private JTextField jTextFieldDenominacion = null;
	private JPanel jPanelSpace = null;
	private JLabel jLabelSuperficie = null;
	private JTextField jTextFieldSuperficie = null;
	private JLabel jLabelValor = null;
	private JTextField jTextFieldValor = null;
	private JPanel jPanelBonificacion = null;
	private JLabel jLabelCodigo = null;
	private ComboBoxEstructuras jComboBoxCodigo = null;
	private JLabel jLabelAnio = null;
	private JTextField jTextFieldAnio = null;
	private JLabel jLabelNumOrden = null;
	private JTextField jTextFieldNumOrden = null;
	private JLabel jLabelTipoSubparcela = null;
	private ComboBoxEstructuras jComboBoxNaturaleza = null;
	private ComboBoxEstructuras jComboBoxTipoSubparcela = null;
	
    private boolean okPressed = false;
	
	public CultivoExtendedInfoPanel()
    {
        super();
        initialize();       
        
    }
       
    public CultivoExtendedInfoPanel(Cultivo cultivo)
    {
        super();
        initialize();       
        
        loadData (cultivo);
        
    }
    
    public class ListenerTextField implements  DocumentListener{
    	/*
    	 * Método que recibe evento cuando se escribe en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void insertUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldAnio.getDocument()==e.getDocument()){
        		jLabelCodigo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datossubparcela.bonificacion.codigo")));
        	}
        }
    	/*
    	 * Método que recibe evento cuando se borra caracteres en alguno de los jTextField que tienen un escuchador DocumentListener
    	 * Según el campo origen del evento, cambia las etiquetas obligatorias.
    	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
    	 */
        public void removeUpdate(DocumentEvent e){
        	//Panel Base Liquidable
        	if(jTextFieldAnio.getDocument().getLength()<=0 && jComboBoxCodigo.getSelectedIndex()<=0){
        		jLabelCodigo.setText(I18N.get("Expedientes","cultivo.panel.datossubparcela.bonificacion.codigo"));
        	}
        }
        /*
         * Método declarado pero no implementado debido a que no se utiliza, pero su declaración ha de ser obligatoria.
         * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
         */
        public void changedUpdate(DocumentEvent e){        	
        }
    }
    
    public class ListenerCombo implements  ActionListener{
    	/*
    	 * Gestiona los eventos de los ComboBox y cambia adecuadamente las etiquetas de aquellos campos que son obligatorios
    	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    	 */ 
        public void actionPerformed(ActionEvent e){  
        	//Panel Base Liquidable
        	if(jComboBoxCodigo.getSelectedIndex()>0 || jTextFieldAnio.getDocument().getLength()>0){
    			jLabelCodigo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datossubparcela.bonificacion.codigo")));
           	}else{
           		jLabelCodigo.setText(I18N.get("Expedientes","cultivo.panel.datossubparcela.bonificacion.codigo"));
        	}
        	
        }
    }
    

    private void initialize()
    {
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0);
        gridBagConstraints4.gridheight = 1;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints(0, 0, 1, 1, 0.5, 0.5, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0);
        gridBagConstraints2.gridheight = 1;
        Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.catastro.intercambio.language.Expedientesi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("Expedientes",bundle);
                
        this.setLayout(new GridBagLayout());
        this.setSize(new Dimension(600, 500));
        this.setPreferredSize(new Dimension(600,500));
        this.add(getJPanelDatosIdentificacion(), gridBagConstraints4);
        this.add(getJPanelDatosSubparcela(), 
                new GridBagConstraints(0,3,1,1,0.5, 0.5,GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0,5,0,5),0,0));
        
        if (blackboard.get("ListaCategoriaCultivo")==null)
        {
            EdicionOperations oper = new EdicionOperations();
            ArrayList lst = oper.obtenerCategoriaCultivo();
            blackboard.put("ListaCategoriaCultivo", lst);
            EdicionUtils.cargarLista(getJComboBoxCalifCultivo(), lst);
        }
        else
        {
            EdicionUtils.cargarLista(getJComboBoxCalifCultivo(), 
                    (ArrayList)blackboard.get("ListaCategoriaCultivo"));
        }        
    
    }
    
    
    public void loadData(Cultivo cultivo)
    {
        if (cultivo !=null)
    	{
    		jTextFieldRefCatastral1.setText(((Cultivo)cultivo).getIdCultivo().getParcelaCatastral().getRefCatastral1());
    		jTextFieldRefCatastral2.setText(((Cultivo)cultivo).getIdCultivo().getParcelaCatastral().getRefCatastral2());
    		
    		if(((Cultivo)cultivo).getTipoSuelo()!=null)
    			jComboBoxNaturaleza.setSelectedPatron(((Cultivo)cultivo).getTipoSuelo());
    		else
    			jComboBoxNaturaleza.setSelectedIndex(0);                                        
    		
    		jTextFieldSubparcela.setText(((Cultivo)cultivo).getCodSubparcela());
    		//if(((Cultivo)cultivo).getIdCultivo().getCalifCultivo()!=null){
    		//	jComboBoxCalifCultivo.setSelectedItem(((Cultivo)cultivo).getIdCultivo().getCalifCultivo());
    		//}
    		if(((Cultivo)cultivo).getIdCultivo().getCalifCultivo()!=null){
            	ArrayList lst = (ArrayList)blackboard.get("ListaCategoriaCultivo");
            	EstructuraDB eTipoCultivo = obtenerElementoLista(lst, ((Cultivo)cultivo).getIdCultivo().getCalifCultivo());
            	jComboBoxCalifCultivo.setSelectedItem(eTipoCultivo);
        	}
    		jTextFieldNumOrden.setText(((Cultivo)cultivo).getIdCultivo().getNumOrden());
    		    		
    		jTextFieldDenominacion.setText(((Cultivo)cultivo).getDenominacionCultivo());
    		
    		if (((Cultivo)cultivo).getIntensidadProductiva() != null){
    			String intensidadProductiva = ((Cultivo)cultivo).getIntensidadProductiva().toString();
    			intensidadProductiva = GeopistaFunctionUtils.completarConCeros(intensidadProductiva, 2);

    			jComboBoxIntensidad.setSelectedItem(intensidadProductiva);
    		}
    		
    		if (((Cultivo)cultivo).getSuperficieParcela()!=null){
    			jTextFieldSuperficie.setText(((Cultivo)cultivo).getSuperficieParcela().toString());
    		}
    		else{
    			jTextFieldSuperficie.setText("");
    		}
    		
    		if (((Cultivo)cultivo).getValorCatastral()!=null){
    			jTextFieldValor.setText(((Cultivo)cultivo).getValorCatastral().toString());
    		}
    		else{
    			jTextFieldValor.setText("");
    		}
    		jComboBoxCodigo.addActionListener(new ListenerCombo());
    		jComboBoxCodigo.setSelectedPatron(((Cultivo)cultivo).getCodBonificacion());
    		jTextFieldAnio.getDocument().addDocumentListener(new ListenerTextField());
    		if (((Cultivo)cultivo).getEjercicioFinBonificacion()!=null){
    			jTextFieldAnio.setText(((Cultivo)cultivo).getEjercicioFinBonificacion().toString());
    		}
    		else{
    			jTextFieldAnio.setText("");
    		}
    		
    		jComboBoxTipoSubparcela.setSelectedPatron(((Cultivo)cultivo).getTipoSubparcela());
        }
    }
    
    /**
     * This method initializes jPanelDatosIdentificacion	
     * 			jPanelDatosIdentificacion.add(getJTextFieldNumOrden(), gridBagConstraints41);
			jPanelDatosIdentificacion.add(getJTextField(), gridBagConstraints32);
			jPanelDatosIdentificacion.add(getJTextFieldCatastral2(), gridBagConstraints22);
	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelDatosIdentificacion()
    {
        if (jPanelDatosIdentificacion == null)
        {       
        	
        	GridBagConstraints gridBagConstraints82 = new GridBagConstraints();
        	gridBagConstraints82.fill = GridBagConstraints.BOTH;
        	gridBagConstraints82.gridy = 1;
        	gridBagConstraints82.weightx = 1.0;
        	gridBagConstraints82.insets = new Insets(0, 5, 0, 5);
        	gridBagConstraints82.gridx = 4;
        	GridBagConstraints gridBagConstraints72 = new GridBagConstraints();
        	gridBagConstraints72.fill = GridBagConstraints.BOTH;
        	gridBagConstraints72.gridy = 4;
        	gridBagConstraints72.weightx = 1.0;
        	gridBagConstraints72.insets = new Insets(0, 5, 0, 5);
        	gridBagConstraints72.gridx = 4;
        	GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
        	gridBagConstraints61.gridx = 4;
        	gridBagConstraints61.gridy = 3;
        	
        	jLabelNumOrden = new JLabel();
        	jLabelNumOrden.setText(I18N.get("Expedientes","cultivo.panel.datosidentificacion.numorden"));
        	
        	GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
        	gridBagConstraints52.fill = GridBagConstraints.BOTH;
        	gridBagConstraints52.gridy = 4;
        	gridBagConstraints52.weightx = 1.0;
        	gridBagConstraints52.insets = new Insets(0, 5, 0, 5);
        	gridBagConstraints52.gridx = 2;
        	GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
        	gridBagConstraints41.fill = GridBagConstraints.BOTH;
        	gridBagConstraints41.gridy = 4;
        	gridBagConstraints41.weightx = 1.0;
        	gridBagConstraints41.insets = new Insets(0, 5, 0, 5);
        	gridBagConstraints41.gridx = 0;
        	GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
        	gridBagConstraints38.gridx = 2;
        	gridBagConstraints38.gridy = 3;
        	
        	jTipoCultivoLabel = new JLabel();
        	jTipoCultivoLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datosidentificacion.califcultivo")));
        	
        	GridBagConstraints gridBagConstraints210 = new GridBagConstraints();
        	gridBagConstraints210.gridx = 0;
        	gridBagConstraints210.gridy = 3;
        	
        	jSubparcelaLabel = new JLabel();
        	jSubparcelaLabel.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datosidentificacion.subparcela")));
        	
        	GridBagConstraints gridBagConstraints110 = new GridBagConstraints();
        	gridBagConstraints110.gridx = 0;
        	gridBagConstraints110.gridy = 2;
        	GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
        	gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
        	gridBagConstraints81.gridy = 4;
        	gridBagConstraints81.weightx = 1.0;
        	gridBagConstraints81.gridx = 1;
        	GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
        	gridBagConstraints71.fill = GridBagConstraints.HORIZONTAL;
        	gridBagConstraints71.gridy = 1;
        	gridBagConstraints71.weightx = 1.0;
        	gridBagConstraints71.insets = new Insets(0, 5, 0, 5);
        	gridBagConstraints71.gridx = 2;
        	GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
        	gridBagConstraints13.fill = GridBagConstraints.BOTH;
        	gridBagConstraints13.gridy = 1;
        	gridBagConstraints13.weightx = 1.0;
        	gridBagConstraints13.insets = new Insets(0, 5, 0, 5);
        	gridBagConstraints13.gridx = 0;
        	GridBagConstraints gridBagConstraints51 = new GridBagConstraints(1, 7, 2, 1, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);            
            gridBagConstraints51.gridy = 1;           
            gridBagConstraints51.gridx = 7;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 6;
            gridBagConstraints31.gridwidth = 2;
            gridBagConstraints31.gridy = 0;            
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints(1, 5, 2, 1, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);            
            gridBagConstraints21.gridy = 1;            
            gridBagConstraints21.gridx = 5;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints(0, 2, 2, 1, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints11.gridx = 4;
            gridBagConstraints11.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints(3, 0, 1, 1, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints1.gridx = 2;
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints(1, 0, 1, 1, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
                    	
            jLabelCargo = new JLabel("", JLabel.CENTER);
            jLabelCargo.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datosidentificacion.naturaleza")));
            jLabelRefCatastral1 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral1.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datosidentificacion.catastral1"))); 
            jLabelRefCatastral2 = new JLabel("", JLabel.CENTER); 
            jLabelRefCatastral2.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datosidentificacion.catastral2")));
            
            jPanelDatosIdentificacion = new JPanel(new GridBagLayout());
            jPanelDatosIdentificacion.setPreferredSize(new Dimension(50, 20));
            jPanelDatosIdentificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "cultivo.panel.datosidentificacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
            
            
            jPanelDatosIdentificacion.add(jLabelRefCatastral1, 
                    new GridBagConstraints(0,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosIdentificacion.add(jLabelRefCatastral2, 
                    new GridBagConstraints(2,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            
            jPanelDatosIdentificacion.add(jLabelCargo, 
                    new GridBagConstraints(4,0,1,1,0.5, 0,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0,5,0,5),0,0));
            jPanelDatosIdentificacion.add(getJTextFieldRefCatastral1(), gridBagConstraints13);
            
			jPanelDatosIdentificacion.add(getJTextFieldRefCatastral2(), gridBagConstraints71);
			jPanelDatosIdentificacion.add(getJSpacePanel(), gridBagConstraints110);
			jPanelDatosIdentificacion.add(jSubparcelaLabel, gridBagConstraints210);
			jPanelDatosIdentificacion.add(jTipoCultivoLabel, gridBagConstraints38);
			jPanelDatosIdentificacion.add(getJTextFieldSubparcela(), gridBagConstraints41);
			jPanelDatosIdentificacion.add(getJComboBoxCalifCultivo(), gridBagConstraints52);
			jPanelDatosIdentificacion.add(jLabelNumOrden, gridBagConstraints61);
			jPanelDatosIdentificacion.add(getJTextFieldNumOrden(), gridBagConstraints72);
			jPanelDatosIdentificacion.add(getJComboBoxNaturaleza(), gridBagConstraints82);
        	
        }
        return jPanelDatosIdentificacion;
    }
    
  
	/**
	 * This method initializes jPanelDatosSubparcela	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelDatosSubparcela() {
		if (jPanelDatosSubparcela == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.gridy = 6;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints23.anchor = GridBagConstraints.NORTH;
			gridBagConstraints23.gridx = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 5;
			jLabelTipoSubparcela = new JLabel();
			jLabelTipoSubparcela.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datossubparcela.tiposubparcela")));
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints12.gridy = 4;
			gridBagConstraints12.gridheight = 5;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 8;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints10.anchor = GridBagConstraints.NORTH;
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 7;
			jLabelValor = new JLabel();
			jLabelValor.setText(I18N.get("Expedientes","cultivo.panel.datossubparcela.valor"));
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 4;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints8.anchor = GridBagConstraints.NORTH;
			gridBagConstraints8.gridx = 0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 3;
			jLabelSuperficie = new JLabel();
			jLabelSuperficie.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datossubparcela.superficie")));
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.BOTH;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints5.gridx = 0;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 2;
			gridBagConstraints18.gridy = 0;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 1;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 1;
			gridBagConstraints16.gridy = 0;
			jLabelIntensidad = new JLabel("", JLabel.CENTER);
			jLabelIntensidad.setText(UtilRegistroExp.getLabelConAsterisco(I18N.get("Expedientes","cultivo.panel.datossubparcela.intensidad")));
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.gridy = 0;
			jLabelDenominacion = new JLabel("", JLabel.CENTER);
			jLabelDenominacion.setText(I18N.get("Expedientes","cultivo.panel.datossubparcela.denominacion"));
			jPanelDatosSubparcela = new JPanel();
			jPanelDatosSubparcela.setLayout(new GridBagLayout());
			jPanelDatosSubparcela.setPreferredSize(new Dimension(200, 100));
			jPanelDatosSubparcela.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "cultivo.panel.datossubparcela.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
			jPanelDatosSubparcela.add(jLabelDenominacion, gridBagConstraints14);
			jPanelDatosSubparcela.add(jLabelIntensidad, gridBagConstraints16);
			jPanelDatosSubparcela.add(getJComboBoxIntensidad(), gridBagConstraints17);
			jPanelDatosSubparcela.add(getJTextFieldDenominacion(), gridBagConstraints5);
			jPanelDatosSubparcela.add(getJPanelSpace(), gridBagConstraints6);
			jPanelDatosSubparcela.add(jLabelSuperficie, gridBagConstraints7);
			jPanelDatosSubparcela.add(getJTextFieldSuperficie(), gridBagConstraints8);
			jPanelDatosSubparcela.add(jLabelValor, gridBagConstraints9);
			jPanelDatosSubparcela.add(getJTextFieldValor(), gridBagConstraints10);
			jPanelDatosSubparcela.add(getJPanelBonificacion(), gridBagConstraints12);
			jPanelDatosSubparcela.add(jLabelTipoSubparcela, gridBagConstraints3);
			jPanelDatosSubparcela.add(getJComboBoxTipoSubparcela(), gridBagConstraints23);
		}
		return jPanelDatosSubparcela;
	}

	/**
	 * This method initializes jTextFieldRefCatastral1	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldRefCatastral1() {
		if (jTextFieldRefCatastral1 == null) {
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
	private JTextField getJTextFieldRefCatastral2() {
		if (jTextFieldRefCatastral2 == null) {
			jTextFieldRefCatastral2 = new TextField(7);
			jTextFieldRefCatastral2.setEditable(false);
		}
		return jTextFieldRefCatastral2;
	}

	
	/**
	 * This method initializes jComboBoxIntensidad	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxIntensidad() {
	    if (jComboBoxIntensidad == null) {
	        jComboBoxIntensidad = new JComboBox();
	        jComboBoxIntensidad.removeAllItems();
	        
	        //escribe de 00 a 99 
	        int i=0;
	        NumberFormat formatter = new DecimalFormat("00");
	        while (i<100)            
	            jComboBoxIntensidad.addItem(formatter.format(new Integer(i++)));
	        
	    }
	    return jComboBoxIntensidad;
	}

	public String validateInput()
    {
        return null; 
    }
    
    public void okPressed()
    {
       okPressed = true;
    }

	/**
	 * This method initializes jSpacePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJSpacePanel() {
		if (jSpacePanel == null) {
			jSpacePanel = new JPanel();
			jSpacePanel.setLayout(new GridBagLayout());
		}
		return jSpacePanel;
	}

	/**
	 * This method initializes jTextFieldSubparcela	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSubparcela() {
		if (jTextFieldSubparcela == null) {
			jTextFieldSubparcela = new TextField(4);
		}
		return jTextFieldSubparcela;
	}

	/**
	 * This method initializes jComboBoxCalifCultivo	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
		
	private JComboBox getJComboBoxCalifCultivo() {
		if (jComboBoxCalifCultivo == null) {
			jComboBoxCalifCultivo = new JComboBox();
            jComboBoxCalifCultivo.setRenderer(new EstructuraDBListCellRenderer());       
		}
		return jComboBoxCalifCultivo;
	}

	/**
	 * This method initializes jTextFieldDenominacion	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDenominacion() {
		if (jTextFieldDenominacion == null) {
			jTextFieldDenominacion = new TextField(40);
		}
		return jTextFieldDenominacion;
	}

	/**
	 * This method initializes jPanelSpace	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSpace() {
		if (jPanelSpace == null) {
			jPanelSpace = new JPanel();
			jPanelSpace.setLayout(new GridBagLayout());
		}
		return jPanelSpace;
	}

	/**
	 * This method initializes jTextFieldSuperficie	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSuperficie() {
	    if (jTextFieldSuperficie == null) {
	        jTextFieldSuperficie = new TextField(7);
	        jTextFieldSuperficie.addCaretListener(new CaretListener()
	                {
	            public void caretUpdate(CaretEvent evt)
	            {
	                EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldSuperficie,7, app.getMainFrame());
	            }
	                });
	    }
	    return jTextFieldSuperficie;
	}

	/**
	 * This method initializes jTextFieldValor	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldValor() {
	    if (jTextFieldValor == null) {
	        jTextFieldValor = new TextField(13);
	        jTextFieldValor.addCaretListener(new CaretListener()
	                {
	            public void caretUpdate(CaretEvent evt)
	            {
	                EdicionUtils.chequeaLongYDecimalCampoEdit(jTextFieldValor,13, app.getMainFrame());
	            }
	                });
	    }
	    return jTextFieldValor;
	}

	/**
	 * This method initializes jPanelBonificacion	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBonificacion() {
		if (jPanelBonificacion == null) {
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.BOTH;
			gridBagConstraints22.gridy = 1;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints22.gridx = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 1;
			gridBagConstraints20.gridy = 0;
			jLabelAnio = new JLabel();
			jLabelAnio.setText(I18N.get("Expedientes","cultivo.panel.datossubparcela.bonificacion.anio"));
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.BOTH;
			gridBagConstraints19.gridy = 1;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints19.gridx = 0;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.gridy = 0;
			jLabelCodigo = new JLabel();
			jLabelCodigo.setText(I18N.get("Expedientes","cultivo.panel.datossubparcela.bonificacion.codigo"));
			jPanelBonificacion = new JPanel();
			jPanelBonificacion.setLayout(new GridBagLayout());
			jPanelBonificacion.setPreferredSize(new Dimension(200, 100));
			jPanelBonificacion.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("Expedientes", "cultivo.panel.datossubparcela.bonificacion.titulo"), TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
			jPanelBonificacion.add(jLabelCodigo, gridBagConstraints15);
			jPanelBonificacion.add(getJComboBoxCodigo(), gridBagConstraints19);
			jPanelBonificacion.add(jLabelAnio, gridBagConstraints20);
			jPanelBonificacion.add(getJTextFieldAnio(), gridBagConstraints22);
			
		}
		return jPanelBonificacion;
	}

	/**
	 * This method initializes jComboBoxCodigo	
	 * 	
	 * @return ComboBoxEstructuras	
	 */
	private ComboBoxEstructuras getJComboBoxCodigo() {
		if (jComboBoxCodigo == null) {
			
            Estructuras.cargarEstructura("Código de bonificación");
            jComboBoxCodigo = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
		}
		jComboBoxCodigo.addActionListener(new ListenerCombo());
		return jComboBoxCodigo;
	}

	/**
	 * This method initializes jTextFieldAnio	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldAnio() {
		if (jTextFieldAnio == null) {
			jTextFieldAnio = new TextField(4);
            jTextFieldAnio.addCaretListener(new CaretListener()
                    {
                public void caretUpdate(CaretEvent evt)
                {
                    EdicionUtils.chequeaLongYNumCampoEdit(jTextFieldAnio,4, app.getMainFrame());
                }
                    });
            
		}
		jTextFieldAnio.getDocument().addDocumentListener(new ListenerTextField());
		return jTextFieldAnio;
	}

	/**
	 * This method initializes jTextFieldNumOrden	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNumOrden() {
		if (jTextFieldNumOrden == null) {
			jTextFieldNumOrden = new TextField(4);
		}
		jTextFieldNumOrden.setName("numeroorden");
		return jTextFieldNumOrden;
	}

	/**
	 * This method initializes jComboBoxNaturaleza	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private ComboBoxEstructuras getJComboBoxNaturaleza() {
		if (jComboBoxNaturaleza == null) {
			Estructuras.cargarEstructura("Naturaleza del suelo");
            jComboBoxNaturaleza = new ComboBoxEstructuras(Estructuras.getListaTipos(),
                    null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
            
		}
		return jComboBoxNaturaleza;
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
			        null, app.getString(AppContext.GEOPISTA_LOCALE_KEY,"es_ES"), true);
			
		}
		return jComboBoxTipoSubparcela;
	}
	
	
	public Cultivo getCultivo(Cultivo cultivo)
	{
		
		if(okPressed)
		{
            if(cultivo==null)
            {
                cultivo = new Cultivo();
            }
            cultivo.setCodSubparcela(jTextFieldSubparcela.getText());
			cultivo.setTipoSuelo(jComboBoxNaturaleza.getSelectedPatron());
			cultivo.setTipoSubparcela(jComboBoxTipoSubparcela.getSelectedPatron());
			cultivo.setCodSubparcela(jTextFieldSubparcela.getText());
			cultivo.setDenominacionCultivo(jTextFieldDenominacion.getText());
			if (jComboBoxIntensidad.getSelectedItem() != null)
				//cultivo.setIntensidadProductiva(new Integer(((EstructuraDB)jComboBoxIntensidad.getSelectedItem()).getPatron()));
				cultivo.setIntensidadProductiva(new Integer(jComboBoxIntensidad.getSelectedItem().toString()));
			if (jTextFieldSuperficie.getText().length() > 0)
				cultivo.setSuperficieParcela(new Long(jTextFieldSuperficie.getText()));
			cultivo.getIdCultivo().getParcelaCatastral().setRefCatastral1(jTextFieldRefCatastral1.getText());
			cultivo.getIdCultivo().getParcelaCatastral().setRefCatastral2(jTextFieldRefCatastral2.getText());
			if (jComboBoxCalifCultivo.getSelectedItem() != null)
				cultivo.getIdCultivo().setCalifCultivo(((EstructuraDB)jComboBoxCalifCultivo.getSelectedItem()).getPatron());
			cultivo.getIdCultivo().setNumOrden(jTextFieldNumOrden.getText());
			
					
			if (jTextFieldValor.getText().length() > 0)
				cultivo.setValorCatastral(Double.valueOf(jTextFieldValor.getText()));
			cultivo.setCodBonificacion(jComboBoxCodigo.getSelectedPatron());
			if (jTextFieldAnio.getText().length() > 0)
				cultivo.setEjercicioFinBonificacion(Integer.valueOf(jTextFieldAnio.getText()));
		}
        else
        {
            cultivo = null;
        }

        return cultivo;
	}

    public boolean datosMinimosYCorrectos()
    {
    	boolean okBonif=true;
    	if(jTextFieldAnio.getDocument().getLength()>0 || jComboBoxCodigo.getSelectedIndex()>0){
    		okBonif = (jComboBoxCodigo!=null && jComboBoxCodigo.getSelectedItem()!=null && jComboBoxCodigo.getSelectedIndex()>0);
    		if(jTextFieldAnio.getDocument().getLength()>0 && jTextFieldAnio.getDocument().getLength()!=4){
        		JOptionPane.showMessageDialog(this,I18N.get("Expedientes", "Error.J10"));
        		okBonif=false;
    		}
    	}
        return  okBonif && (jTextFieldRefCatastral1.getText()!=null && !jTextFieldRefCatastral1.getText().equalsIgnoreCase(""))&&
        		(jTextFieldRefCatastral2.getText()!=null && !jTextFieldRefCatastral2.getText().equalsIgnoreCase(""))&&
        		(jTextFieldSubparcela.getText()!=null && !jTextFieldSubparcela.getText().equalsIgnoreCase(""))&&
                (jTextFieldSuperficie.getText()!=null && !jTextFieldSuperficie.getText().equalsIgnoreCase(""))&&
                (jComboBoxNaturaleza.getSelectedItem()!=null && jComboBoxNaturaleza.getSelectedIndex()>0) &&
                (jComboBoxTipoSubparcela.getSelectedItem()!=null && jComboBoxTipoSubparcela.getSelectedIndex()>0) &&
                (jComboBoxIntensidad.getSelectedItem()!=null && jComboBoxIntensidad.getSelectedIndex()>-1) &&
                (jComboBoxCalifCultivo.getSelectedItem()!=null && !((EstructuraDB)jComboBoxCalifCultivo.getSelectedItem()).getPatron().equalsIgnoreCase(""));
    }

    public boolean datosMinimosYCorrectosComun()
    {
    	boolean okBonif=true;
    	if(jTextFieldAnio.getDocument().getLength()>0 || jComboBoxCodigo.getSelectedIndex()>0){
    		okBonif = (jComboBoxCodigo!=null && jComboBoxCodigo.getSelectedItem()!=null && jComboBoxCodigo.getSelectedIndex()>0);
    	}
        return  okBonif && (jTextFieldRefCatastral1.getText()!=null && !jTextFieldRefCatastral1.getText().equalsIgnoreCase(""))&&
        		(jTextFieldRefCatastral2.getText()!=null && !jTextFieldRefCatastral2.getText().equalsIgnoreCase(""))&&
        		(jTextFieldSubparcela.getText()!=null && !jTextFieldSubparcela.getText().equalsIgnoreCase(""))&&        
                (jTextFieldSuperficie.getText()!=null && !jTextFieldSuperficie.getText().equalsIgnoreCase(""))&&
                (jComboBoxNaturaleza.getSelectedItem()!=null && jComboBoxNaturaleza.getSelectedIndex()>0) &&
                (jComboBoxTipoSubparcela.getSelectedItem()!=null && jComboBoxTipoSubparcela.getSelectedIndex()>0) &&
                (jComboBoxIntensidad.getSelectedItem()!=null && jComboBoxIntensidad.getSelectedIndex()>-1) &&
                (jComboBoxCalifCultivo.getSelectedItem()!=null && !((EstructuraDB)jComboBoxCalifCultivo.getSelectedItem()).getPatron().equalsIgnoreCase(""));
    }

    public Cultivo datosMinimosParaReparto(Cultivo cultivo)
    {
        cultivo.getIdCultivo().setCalifCultivo(((EstructuraDB)jComboBoxCalifCultivo.getSelectedItem()).getPatron());
        cultivo.setCodSubparcela(jTextFieldSubparcela.getText());
        return cultivo;
    }
    
    private EstructuraDB obtenerElementoLista(ArrayList lst, String patron) {
		
    	for (Iterator iteratorLista = lst.iterator();iteratorLista.hasNext();){
    		EstructuraDB estructura = (EstructuraDB)iteratorLista.next();
    		if(estructura.getPatron().equals(patron))
    			return estructura;
    	}
		return null;
	}
    
}  //  @jve:decl-index=0:visual-constraint="10,10" 
