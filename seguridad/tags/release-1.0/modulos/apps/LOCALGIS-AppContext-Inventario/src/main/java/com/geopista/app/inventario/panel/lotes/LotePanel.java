package com.geopista.app.inventario.panel.lotes;


import javax.swing.JPanel;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;

import com.geopista.app.inventario.component.InventarioLabel;
import com.geopista.app.inventario.component.InventarioTextField;
import com.geopista.app.inventario.component.InventarioTextPane;
import com.geopista.app.inventario.panel.GestionDocumentalJPanel;

import com.geopista.app.utilidades.JNumberTextField;
import com.geopista.app.utilidades.TextPane;

import com.geopista.protocol.inventario.Lote;

public class LotePanel extends JPanel{
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(LotePanel.class);


	private Lote lote;
	private AppContext aplicacion;
    /**
	 * 
	 */
	
	public LotePanel( Lote lote , boolean documentacion)throws Exception {
         this.aplicacion= (AppContext) AppContext.getApplicationContext();
         initComponents(documentacion);
         renombrarComponentes();
         load(lote);
    }
    public void initComponents(boolean documentacion) throws Exception{
    	nombreJLabel= new InventarioLabel();
    	nombreJTextField =new InventarioTextField(100);
       
    	//seguroJLabel = new InventarioLabel();
        //seguroJTextField = new InventarioTextField(100);
        
        descripcionJLabel = new InventarioLabel();
        descripcionJScroolPane = new javax.swing.JScrollPane(); 
        descripcionJTextPane = new InventarioTextPane(1000);
        descripcionJScroolPane.setViewportView(descripcionJTextPane);
        
        destinoJLabel = new InventarioLabel();
        destinoJTextField = new InventarioTextField(255);
        
        numeroJLabel = new InventarioLabel();
        numeroJTextField =  new com.geopista.app.utilidades.JNumberTextField(JNumberTextField.NUMBER, new Long(9999), true, 0);
        numeroJTextField.setSignAllowed(false);
    
        
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        
        
        add(nombreJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 100, 20));
        add(nombreJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 300, 20));
        //add(seguroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 100, 20));
        //add(seguroJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 35, 300, 20));
        add(destinoJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 100, 20));
        add(destinoJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 35, 300, 20));
        add(numeroJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 85, 300, 20));
        add(numeroJTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 85, 60, 20));
        add(descripcionJLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, 20));
        descripcionJScroolPane.setViewportView(descripcionJTextPane);
        add(descripcionJScroolPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20,135, 400, 60));
     	documentosJPanel = new GestionDocumentalJPanel(false);
        
        if (documentacion){      
         	add(documentosJPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 400, 400));
        } 
    }
    public void clear(){
        nombreJTextField.setText("");
        //seguroJTextField.setText("");
        destinoJTextField.setText("");
        descripcionJTextPane.setText("");
        numeroJTextField.setText("0");
      }
    public void renombrarComponentes(){
    	
    	nombreJLabel.setText(aplicacion.getI18nString("inventario.lote.nombre")+":");
        //seguroJLabel.setText(aplicacion.getI18nString("inventario.lote.seguro")+":");
        descripcionJLabel.setText(aplicacion.getI18nString("inventario.lote.descripcion")+":");
        destinoJLabel.setText(aplicacion.getI18nString("inventario.lote.destino")+":");
        numeroJLabel.setText(aplicacion.getI18nString("inventario.lote.numero")+":");
    }
    
    public void setEnabled(boolean valor){
    	nombreJTextField.setEnabled(valor);
    	//seguroJTextField.setEnabled(valor);
    	descripcionJTextPane.setEnabled(valor);
    	destinoJTextField.setEnabled(valor);
    	numeroJTextField.setEnabled(valor);
    	documentosJPanel.setEnabled(valor);
    }
    /**
     * Muestra en pantalla los datos de un bien revertible
     * @param bienRevertibleAux
     */
    public void load(Lote loteAux){
    	try{
    		if (loteAux==null)
    			this.lote=new Lote();
    		else
    			this.lote= loteAux;
    		
    		nombreJTextField.setText(lote.getNombre_lote()==null?"":lote.getNombre_lote());
    		//seguroJTextField.setText(lote.getSeguro()==null?"":lote.getSeguro());
    		descripcionJTextPane.setText(lote.getDescripcion()==null?"":lote.getDescripcion());
    		destinoJTextField.setText(lote.getDestino()==null?"":lote.getDestino());
    		documentosJPanel.load(lote.getDocumentos());
    		numeroJTextField.setNumber(lote.getNumeroBienes());
    		if (lote.getId_lote()!=null){
    			numeroJTextField.setEnabled(false);
    			documentosJPanel.load(lote);}
    		else {
    			numeroJTextField.setText("1");
    			numeroJTextField.setEnabled(true);
    			documentosJPanel.modificarJButtonSetEnabled(false);
    		}
    		
    	}catch(Exception ex){
    		logger.error("Error al cargar la información de los lotes");
    	}
    }
	
    /**
     * Devuelve los cambios producidos en el bien revertible
     * @return
     */
    public Lote  getLote(){
    	lote.setNombre_lote(nombreJTextField.getText());
    	//lote.setSeguro(seguroJTextField.getText());
    	lote.setDescripcion(descripcionJTextPane.getText());
    	lote.setDestino(destinoJTextField.getText());
    	try{lote.setNumeroBienes(Integer.parseInt(numeroJTextField.getNumber().toString()));}catch (Exception e) {}
    	documentosJPanel.actualizarDatos(lote);
    	return lote;
    }
    
    

    public GestionDocumentalJPanel getDocumentosJPanel() {
		return documentosJPanel;
	}
	public void setDocumentosJPanel(GestionDocumentalJPanel documentosJPanel) {
		this.documentosJPanel = documentosJPanel;
	}



	private javax.swing.JLabel nombreJLabel;
    private javax.swing.JTextField nombreJTextField;
    
    //private javax.swing.JLabel seguroJLabel;
    //private javax.swing.JTextField seguroJTextField;
    
    private javax.swing.JLabel numeroJLabel;
    private com.geopista.app.utilidades.JNumberTextField numeroJTextField;
    
    private javax.swing.JLabel descripcionJLabel;
    private javax.swing.JScrollPane descripcionJScroolPane;
    private TextPane descripcionJTextPane;
    
    private javax.swing.JLabel destinoJLabel;
    private javax.swing.JTextField destinoJTextField;
    private GestionDocumentalJPanel documentosJPanel;
      
}
