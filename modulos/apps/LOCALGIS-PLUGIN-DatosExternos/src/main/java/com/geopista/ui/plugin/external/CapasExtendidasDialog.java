/**
 * CapasExtendidasDialog.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geopista.app.AppContext;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;


public class CapasExtendidasDialog extends JDialog {
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();  
	
	private JRadioButton rdbModoCargar = null;
    private JRadioButton rdbModoActualizar = null;
    private JRadioButton rdbModoEliminar = null;
    private ButtonGroup btnGroup = null;
    private JPanel jPanelRadio = null;
       
    private JList jListCapasExtendidas = null;
    private JScrollPane scrollCapasExtendidas = null;
    private DefaultListModel listmodel = new DefaultListModel();
    
    private JButton btnAceptar = null;
    private JButton btnCancelar = null;
    
    String string1 = I18N.get("ConfigureQueryExternalDataSource.si"); 
    String string2 = I18N.get("ConfigureQueryExternalDataSource.no"); 
    Object[] options = {string1, string2};
    
    public CapasExtendidasDialog(final Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        initComponents();
    } 

	private void initComponents() {
        btnGroup = new ButtonGroup();
        btnGroup.add(getRdbModoCargar());
        btnGroup.add(getRdbModoActualizar());
        btnGroup.add(getRdbModoEliminar());
        
        jPanelRadio = getJPanelRadio();
        jPanelRadio.setLayout(null);
        jPanelRadio.add(getRdbModoCargar(), null);
        jPanelRadio.add(getRdbModoActualizar(), null);
        jPanelRadio.add(getRdbModoEliminar(), null);
        
        scrollCapasExtendidas = getScrollCapasExtendidas();
        
        getContentPane().setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        getContentPane().add(jPanelRadio, null);
        getContentPane().add(scrollCapasExtendidas, null);
        getContentPane().add(getBtnAceptar(), null);
        getContentPane().add(getBtnCancelar(), null);
        getBtnAceptar().setEnabled(false);
        pack();      
        setSize(new Dimension(500,300));
        jListCapasExtendidas.addListSelectionListener(new ValueReporter());
    	
    } 
	
    private class ValueReporter implements ListSelectionListener {
    	public void valueChanged(ListSelectionEvent event) {
    		if (!event.getValueIsAdjusting()){
    				
    		}
    	}
    }
    
    private JRadioButton getRdbModoCargar(){
        if (rdbModoCargar == null){
            rdbModoCargar = new JRadioButton();
            rdbModoCargar.setText(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.cargar"));
            rdbModoCargar.setBounds(new Rectangle(1,1,200,15));
            rdbModoCargar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	getBtnAceptar().setEnabled(true);
                }
            });
        }
        return rdbModoCargar;
    }
	
    private JRadioButton getRdbModoActualizar(){
        if (rdbModoActualizar == null){
            rdbModoActualizar = new JRadioButton();
            rdbModoActualizar.setText(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.actualizar"));
            rdbModoActualizar.setBounds(new Rectangle(1,31,200,15));
            rdbModoActualizar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	getBtnAceptar().setEnabled(true);
                }
            });
        }
        return rdbModoActualizar;
    }
	
    private JRadioButton getRdbModoEliminar(){
        if (rdbModoEliminar == null){
            rdbModoEliminar = new JRadioButton();
            rdbModoEliminar.setText(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.eliminar"));
            rdbModoEliminar.setBounds(new Rectangle(1,61,200,15));
            rdbModoEliminar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	getBtnAceptar().setEnabled(true);
                }
            });
        }
        return rdbModoEliminar;
    }

    public JPanel getJPanelRadio(){
        if (jPanelRadio == null){
            jPanelRadio = new JPanel();
            jPanelRadio.setBounds(new Rectangle(250,60,200,100));
        }
        return jPanelRadio;
    }
    
    private JScrollPane getScrollCapasExtendidas(){
        if (scrollCapasExtendidas == null){
        	scrollCapasExtendidas = new JScrollPane();
        	scrollCapasExtendidas.setBounds(new Rectangle(20,20,200,220));   
        	scrollCapasExtendidas.setViewportView(getLstCapasExtendidas());
        	scrollCapasExtendidas.setBorder(BorderFactory.createTitledBorder(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.scroll")));
        }
        else{
        	scrollCapasExtendidas.setViewportView(getLstCapasExtendidas());
        }
        return scrollCapasExtendidas;
    }
    
    public JList getLstCapasExtendidas(){
        if (jListCapasExtendidas == null){ 
        	jListCapasExtendidas = new JList(listmodel);
            
        	CapasExtendidasListCellRenderer renderer = new CapasExtendidasListCellRenderer();
        	jListCapasExtendidas.setCellRenderer(renderer);
            
        	CapasExtendidasDAO capasExtendidasDAO = new CapasExtendidasDAO();
            Vector vectorCapasExtendidas = null;

            vectorCapasExtendidas = capasExtendidasDAO.listCapasExtendidas();
            
            listmodel.removeAllElements();
            if (vectorCapasExtendidas!= null){
            	for (int i=0; i< vectorCapasExtendidas.size(); i++){
            		listmodel.addElement(vectorCapasExtendidas.get(i));   
            	}
            }
        }
        else{
        	CapasExtendidasDAO capasExtendidasDAO = new CapasExtendidasDAO();
            Vector vectorCapasExtendidas = null;

            vectorCapasExtendidas = capasExtendidasDAO.listCapasExtendidas();

            if (listmodel!=null && listmodel.getSize()!=0)
                listmodel.removeAllElements();
            if (vectorCapasExtendidas!= null){
            	for (int i=0; i< vectorCapasExtendidas.size(); i++){
            		listmodel.addElement(vectorCapasExtendidas.get(i));   
            	}
            }
        }
        return jListCapasExtendidas;
    }
    
    public JButton getBtnAceptar(){
        if (btnAceptar == null)
        {
        	btnAceptar = new JButton();
        	btnAceptar.setBounds(new Rectangle(240, 185, 100, 25));
        	btnAceptar.setText(I18N.get("ConfigureQueryExternalDataSource.botonAceptar"));
        	btnAceptar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	btnAceptar_actionPerformed(e);
                }
        	});
        }
        return btnAceptar;
    }
    
    public JButton getBtnCancelar(){
        if (btnCancelar == null)
        {
        	btnCancelar = new JButton();
        	btnCancelar.setBounds(new Rectangle(370, 185, 100, 25));
        	btnCancelar.setText(I18N.get("ConfigureQueryExternalDataSource.botonCancelar"));
        	btnCancelar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                	btnCancelar_actionPerformed(e);
                }
        	});
        }
        return btnCancelar;
    }
    
    public static JRadioButton getSelectedRadioButton(ButtonGroup group){
        if (group !=null){
        	for (Enumeration e=group.getElements(); e.hasMoreElements(); ){
        		JRadioButton b = (JRadioButton)e.nextElement();
        		if (b.getModel() == group.getSelection()){
        			return b;
        		}
        	}
        }
       	return null;
        
    }
    
	private void btnAceptar_actionPerformed(ActionEvent e) {
		CapasExtendidas capaExtendida = (CapasExtendidas) jListCapasExtendidas.getSelectedValue();
		final CapasExtendidasDAO capasExtendidasDAO = new CapasExtendidasDAO();
		final CapasExtendidas capaExtendida2 = capasExtendidasDAO.getCapaExtendida(capaExtendida.getId_layer());
		final String nombreTabla = capaExtendida2.getNombreTabla();
		if (getSelectedRadioButton(btnGroup) == rdbModoEliminar){
			int n = JOptionPane.showOptionDialog(this,
			I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.eliminar.mensaje"),
   	                "",
   	                JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
            if (n==JOptionPane.YES_OPTION){
        		/* ponemos una pantalla con el reloj */
                final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
                progressDialog.setTitle(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.titulo"));
                progressDialog.report(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.contenido"));

                progressDialog.addComponentListener(new ComponentAdapter(){
                    public void componentShown(ComponentEvent e){
                        new Thread(new Runnable(){
                            public void run(){
                                try{
                    	        	if (eliminarDatos(capasExtendidasDAO, nombreTabla)){
                    	        		lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.eliminar.mensaje.exito"), 1);
                    	        	}
                    	        	else{
                    	        		lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.eliminar.mensaje.fracaso"), 2);
                    	        	}
                                }
                                catch(Exception e){
                                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
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
		}
		else{
			if (getSelectedRadioButton(btnGroup) == rdbModoCargar){
        		/* ponemos una pantalla con el reloj */
                final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
                progressDialog.setTitle(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.titulo"));
                progressDialog.report(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.contenido"));

                progressDialog.addComponentListener(new ComponentAdapter(){
                    public void componentShown(ComponentEvent e){
                        new Thread(new Runnable(){
                            public void run(){
                                try{
                    				if (capasExtendidasDAO.contieneDatosCapaExtendida(nombreTabla)){
                    					lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.grabar.mensaje.contieneDatos"), 2);
                    				}
                    				else{
                    					int resultado = grabarDatos(capasExtendidasDAO, capaExtendida2);
                    	        		if (resultado == 1){
                    		        		lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.grabar.mensaje.exito"), 1);
                    		        	}
                    		        	else{
                    		        		if (resultado == 2){
                    		        			lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.grabar.mensaje.vacia"), 2);
                    		        		}
                    		        		else{
                    		        			lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.grabar.mensaje.fracaso"), 2);
                    		        		}
                    		        	}
                    				}
                                }
                                catch(Exception e){
                                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
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
			else{
				if (getSelectedRadioButton(btnGroup) == rdbModoActualizar){
					int n = JOptionPane.showOptionDialog(this,
						I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.modificar.mensaje"),
						"",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
					if (n==JOptionPane.YES_OPTION){
		        		/* ponemos una pantalla con el reloj */
		                final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);
		                progressDialog.setTitle(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.titulo"));
		                progressDialog.report(I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.reloj.contenido"));

		                progressDialog.addComponentListener(new ComponentAdapter(){
		                    public void componentShown(ComponentEvent e){
		                        new Thread(new Runnable(){
		                            public void run(){
		                                try{
		            			        	if (eliminarDatos(capasExtendidasDAO, nombreTabla)){
		            			        		int resultado = grabarDatos(capasExtendidasDAO, capaExtendida2);
		            			        		if (resultado == 1){
		            				        		lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.actualizar.mensaje.exito"), 1);
		            				        	}
		            				        	else{
		            				        		if (resultado == 2){
		            				        			lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.actualizar.mensaje.vacia"), 2);
		            				        		}
		            				        		else{
		            				        			lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.actualizar.mensaje.fracaso"), 2);
		            				        		}
		            				        	}
		            			        	}
		            			        	else{
		            			        		lanzarMensaje("", I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.actualizar.mensaje.fracaso"), 2);
		            			        	}
		                                }
		                                catch(Exception e){
		                                    ErrorDialog.show(aplicacion.getMainFrame(), aplicacion.getI18nString("SQLError.Titulo"), aplicacion.getI18nString("SQLError.Aviso"), StringUtil.stackTrace(e));
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
				}
			}
		}
	}

	private boolean eliminarDatos(CapasExtendidasDAO capasExtendidasDAO, String nombreTabla){
		
    	boolean resultado = capasExtendidasDAO.vaciarCapaExtendida(nombreTabla);
    	
    	return resultado;
	}
	
	private int grabarDatos(CapasExtendidasDAO capasExtendidasDAO, CapasExtendidas capaExtendida){
		int resultado = capasExtendidasDAO.cargarDatosCapaExtendida(capaExtendida);	
	
    	return resultado;
	}
	
	private void btnCancelar_actionPerformed(ActionEvent e) {
        int n = JOptionPane.showOptionDialog(this,
                I18N.get("ConfigureQueryExternalDataSource.capasExtendidas.general.salir.mensaje"),
                "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[1]);
        if (n==JOptionPane.NO_OPTION) return;
        
        this.dispose();
	}

    public void lanzarMensaje(String titulo,String mensaje, int tipo){
        JOptionPane optionPane= new JOptionPane(mensaje, tipo);
        JDialog dialog =optionPane.createDialog(this, "");
        dialog.setTitle(titulo);
        dialog.show();        
    }
}