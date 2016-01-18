/*
 * BotoneraJPanel.java
 *
 * Created on 7 de julio de 2006, 9:48
 */

package com.geopista.app.eiel.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.vividsolutions.jump.I18N;

public class BotoneraJPanel extends JPanel {
    
    private ArrayList actionListeners= new ArrayList();
    private String botonPressed;
    
    private JButton jButtonAniadir = null;
    private JButton jButtonEliminar = null;
    private JButton jButtonModificar = null;
    private JButton jButtonFiltrar = null;
    private JButton jButtonListarTabla = null;
	private JButton jButtonDigitalizar = null;
	private JButton jButtonDesactivar = null;
//	private JButton jButtonInformes = null;
	private JButton jButtonFiltroInformes = null;
	private JButton jButtonExportar = null;
	private JButton jButtonVersionado = null;
	private JCheckBox jCkeckBoxFiltroGeometrias=null;
	private JButton jButtonValidarMPT = null;

	private JButton jButtonValidar = null;
	private JButton jButtonPublicar = null;
	
    public BotoneraJPanel() {
    	Locale loc=I18N.getLocaleAsObject();         
        ResourceBundle bundle = ResourceBundle.getBundle("com.geopista.app.eiel.language.LocalGISEIELi18n",loc,this.getClass().getClassLoader());
        I18N.plugInsResourceBundle.put("LocalGISEIEL",bundle);
        initComponents();
    }
    

    
    public JButton getJButtonAniadir(){
    	
    	if (jButtonAniadir == null){
    		
    		jButtonAniadir = new JButton();
    		jButtonAniadir.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonadd"));
    		jButtonAniadir.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                        annadirJButtonActionPerformed();
                }
            });
    	}
    	return jButtonAniadir;
    	
    }    
    
    public JButton getJButtonModificar(){
    	
    	if (jButtonModificar == null){
    		
    		jButtonModificar = new JButton();
    		jButtonModificar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonmodify"));
    		jButtonModificar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    modificarJButtonActionPerformed();
                }
            });
    	}
    	return jButtonModificar;
    }
    
    public JButton getJButtonEliminar(){
    	
    	if (jButtonEliminar == null){
    		
    		jButtonEliminar = new JButton();
    		jButtonEliminar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttondelete"));
    		jButtonEliminar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    eliminarJButtonActionPerformed();
                }
            });
    	}
    	return jButtonEliminar;
    }
    
    public JButton getJButtonFiltrar(){
    	
    	if (jButtonFiltrar == null){
    		
    		jButtonFiltrar = new JButton();
    		jButtonFiltrar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonfilter"));
    		jButtonFiltrar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    anexarJButtonActionPerformed();
                }
            });
    	}
    	return jButtonFiltrar;
    }
    
    public JButton getJButtonDigitalizar(){
        
        if (jButtonDigitalizar == null){
            
        	jButtonDigitalizar = new JButton();
        	jButtonDigitalizar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.digitalizar"));
        	jButtonDigitalizar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    digitalizarJButtonActionPerformed();
                }
            });
        }
        return jButtonDigitalizar;
    }

    public JButton getJButtonDesactivar(){

    	if (jButtonDesactivar == null){

    		jButtonDesactivar = new JButton();
    		jButtonDesactivar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.desactivar"));
    		jButtonDesactivar.addActionListener(new java.awt.event.ActionListener() {
    			public void actionPerformed(java.awt.event.ActionEvent evt) {
    				desactivarJButtonActionPerformed();
    			}
    		});
    		jButtonDesactivar.setEnabled(false);
    	}
    	return jButtonDesactivar;
    }
    
    public JButton getJButtonListarTabla(){
        
        if (jButtonListarTabla == null){
            
            jButtonListarTabla = new JButton();
            jButtonListarTabla.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonlistar"));
            jButtonListarTabla.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    listarJButtonActionPerformed();
                }
            });
        }
        return jButtonListarTabla;
    }
    
    public JButton getJButtonFichaMunicipal(){
    	JButton button=getJButtonFiltroInformes();
    	button.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.fichamunicipal"));
    	return button;
    }
    
    public JButton getJButtonInformes(){
    	JButton button=getJButtonFiltroInformes();
    	button.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroinformes"));
    	return button;
    }
    
    public JButton getJButtonFiltroInformes(){
    	if (jButtonFiltroInformes == null){            
    		jButtonFiltroInformes = new JButton();
    		jButtonFiltroInformes.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.filtroinformes"));
    		jButtonFiltroInformes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	try {
                		filtroInformesJButtonActionPerformed();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
    		jButtonFiltroInformes.setEnabled(false);
        }
        return jButtonFiltroInformes;
    }
    
    public JButton getJButtonExportar(){
    	if (jButtonExportar == null){            
    		jButtonExportar = new JButton();
    		jButtonExportar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.exportar"));
    		jButtonExportar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	try {
                		exportarJButtonActionPerformed();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
    		jButtonExportar.setEnabled(false);
        }
        return jButtonExportar;
    }
    

	public JButton getjButtonValidarMPT() {
		if (jButtonValidarMPT == null){            
			jButtonValidarMPT = new JButton();
			jButtonValidarMPT.setText(I18N.get("LocalGISEIEL", "localgiseiel.mpt.validate.panel.title"));
			jButtonValidarMPT.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	try {
                		validarMPTJButtonActionPerformed();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            });
			jButtonValidarMPT.setEnabled(true);
			
		}
		return jButtonValidarMPT;
	}



	public void setjButtonValidarMPT(JButton jButtonValidarMPT) {
		this.jButtonValidarMPT = jButtonValidarMPT;
	}
	
    
    public JCheckBox getJCheckBoxFiltroGeometrias(){
    	if (jCkeckBoxFiltroGeometrias == null){            
    		jCkeckBoxFiltroGeometrias = new JCheckBox();
    		jCkeckBoxFiltroGeometrias.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.geometrias"));
    		jCkeckBoxFiltroGeometrias.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	try {
                		listarNoReferenciadosActionPerformed();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }              
            });
        }
        return jCkeckBoxFiltroGeometrias;
    }
    
    
    public JButton getJButtonVersionado(){
    	
    	if (jButtonVersionado == null){
    		
    		jButtonVersionado = new JButton();
    		jButtonVersionado.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.buttonversion"));
    		jButtonVersionado.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    versionJButtonActionPerformed();
                }
            });
    		jButtonVersionado.setEnabled(false);
    	}
    	return jButtonVersionado;
    	
    }
    
    public JButton getJButtonValidar(){
    	if (jButtonValidar== null){            
    		jButtonValidar = new JButton();
    		jButtonValidar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.validar"));
    		jButtonValidar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	try {
                		 botonPressed= ConstantesLocalGISEIEL.OPERACION_VALIDAR;
                		fireActionPerformed();
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            });
    		jButtonValidar.setEnabled(false);
        }
        return jButtonValidar;
    }
    
    public JButton getJButtonPublicar(){
    	if (jButtonPublicar== null){            
    		jButtonPublicar = new JButton();
    		jButtonPublicar.setText(I18N.get("LocalGISEIEL", "localgiseiel.editinginfopanel.publicar"));
    		jButtonPublicar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                	try {
               		 botonPressed= ConstantesLocalGISEIEL.OPERACION_PUBLICAR;
                		fireActionPerformed();
					} catch (Exception e) {
						e.printStackTrace();
					}
                }
            });
    		jButtonPublicar.setEnabled(false);
        }
        return jButtonPublicar;
    }

    private void initComponents() {
    	
    	this.setLayout(new GridBagLayout());
    	
    	this.add(getJButtonAniadir(), 
        		new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
    	
    	this.add(getJButtonModificar(), 
        		new GridBagConstraints(1,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(1,1,1,1),0,0));
    	
    	this.add(getJButtonEliminar(), 
        		new GridBagConstraints(2,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
 
        this.add(getJButtonListarTabla(), 
                new GridBagConstraints(3,0,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        
        this.add(getJButtonDigitalizar(), 
                new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));

        this.add(getJButtonFiltroInformes(), 
                new GridBagConstraints(1,1,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        
        
        this.add(getJButtonExportar(), 
                new GridBagConstraints(2,1,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        
        //Si tiene permisos para ver la versión
        if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VERSION_READ)){
        	this.add(getJButtonVersionado(), new GridBagConstraints(3,1,1,1,0.1, 0.1,
        			GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));        
        	this.add(getJCheckBoxFiltroGeometrias(), new GridBagConstraints(3,2,1,1,0.1, 0.1,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        }
        this.add(getjButtonValidarMPT(), 
                new GridBagConstraints(0,2,1,1,0.1, 0.1,GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        
        if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_VALIDADOR_EIEL))
	        this.add(getJButtonValidar(), 
	                new GridBagConstraints(1,2,1,1,0.1, 0.1,GridBagConstraints.CENTER,
	                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
        
        if (ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_PUBLICADOR_EIEL))
	        this.add(getJButtonPublicar(), 
	                new GridBagConstraints(2,2,1,1,0.1, 0.1,GridBagConstraints.CENTER,
	                        GridBagConstraints.HORIZONTAL, new Insets(5,5,5,5),0,0));
 

    }

    private void eliminarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_ELIMINAR;
        if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_MODIFICA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void anexarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_FILTRAR;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }

    private void annadirJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_ANNADIR;
       	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_MODIFICA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
  
        
    }
    
    private void versionJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_VERSION;
        fireActionPerformed();
    }

    private void modificarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_MODIFICAR;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_MODIFICA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void listarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_LISTAR;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void digitalizarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_DIGITALIZAR;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_MODIFICA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void desactivarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_DESACTIVAR;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_MODIFICA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
        
    private void filtroInformesJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_FILTRO_INFORMES;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void validarMPTJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_VALIDAR_MPT;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarJButtonActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_EXPORTAR;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void listarNoReferenciadosActionPerformed() {
        botonPressed= ConstantesLocalGISEIEL.OPERACION_FILTRAR_REFERENCIADOS;
    	if(ConstantesLocalGISEIEL.permisos.containsKey(ConstantesLocalGISEIEL.PERM_CONSULTA_EIEL))
       		fireActionPerformed();
		else
			JOptionPane.showMessageDialog(
                    getParent(),
                    I18N.get("LocalGISEIEL", "localgiseiel.mensajes.permisos"),
                    null,
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    private static AppContext aplicacion= (AppContext) AppContext.getApplicationContext();
    private static String locale = aplicacion.getUserPreference(AppContext.GEOPISTA_LOCALE_KEY, "es_ES", true);
    
    public void setEnabled(boolean b){
        getJButtonAniadir().setEnabled(b);
        getJButtonModificar().setEnabled(b);
        getJButtonFiltrar().setEnabled(b);
        getJButtonEliminar().setEnabled(b);
        getJButtonVersionado().setEnabled(b);
        getJButtonListarTabla().setEnabled(b);
        getJButtonFiltroInformes().setEnabled(b);
        getJButtonExportar().setEnabled(b);
        getJCheckBoxFiltroGeometrias().setEnabled(b);
        getJButtonDigitalizar().setEnabled(b);
//        getjButtonValidarMPT().setEnabled(b);
    }

    public void annadirJButtonSetEnabled(boolean b){
       getJButtonAniadir().setEnabled(b); 
    }


    public void addActionListener(ActionListener l) {
        this.actionListeners.add(l);
    }

    public void removeActionListener(ActionListener l) {
        this.actionListeners.remove(l);
    }

    private void fireActionPerformed() {
        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener l = (ActionListener) i.next();
            l.actionPerformed(new ActionEvent(this, 0, null));
        }
    }

    public String getBotonPressed(){
        return botonPressed;
    }

    public void setBotonPressed(String s){
        botonPressed= s;
    }

    
    
}
