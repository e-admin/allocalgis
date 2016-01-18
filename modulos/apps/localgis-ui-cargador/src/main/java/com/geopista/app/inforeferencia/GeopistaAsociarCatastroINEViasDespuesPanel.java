/**
 * GeopistaAsociarCatastroINEViasDespuesPanel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inforeferencia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.geopista.app.AppContext;
import com.geopista.ui.images.IconLoader;
import com.geopista.ui.wizard.WizardContext;
import com.geopista.ui.wizard.WizardPanel;
import com.geopista.util.ApplicationContext;
import com.geopista.util.GeopistaUtil;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.InputChangedListener;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

/**
 * GeopistaAsociarCatastroINEViasPanel : Panel que permite enlazar y relacionar
 * los tramos de via del INE con las Vias de Catastro
 */

public class GeopistaAsociarCatastroINEViasDespuesPanel extends JPanel implements
        ListSelectionListener, WizardPanel
{

	Logger logger =Logger.getLogger(GeopistaAsociarCatastroINEViasDespuesPanel.class);
    public static final String NOESPECIFICADO = "NE";
    
    public ApplicationContext aplicacion = AppContext.getApplicationContext();
    private WizardContext wizardContext; 

    private JScrollPane pnlCatastro = new JScrollPane();
    private JScrollPane pnlINE = new JScrollPane();
    private JScrollPane pnlEnlazados = new JScrollPane();

    private JButton btnAsociar = new JButton();
    private JButton btnEliminar = new JButton();   

    private String nextID = null;

    private JLabel lblErrores = new JLabel();
    private JLabel lblImagen = new JLabel();
    private JLabel lblAsociar = new JLabel();
    private JLabel lblINEVias = new JLabel();
    private JLabel lblEnlazados = new JLabel();
    private JLabel lblCatastro = new JLabel();
    
    private ViasIneRenderer viasIneRenderer = new ViasIneRenderer();
    private ViasCatastroDespuesRenderer viasCatastroRenderer = new ViasCatastroDespuesRenderer();
    private ViasCatastroViasINEAssociationDespuesRenderer viasCatastroViasINEAssociationRenderer = new ViasCatastroViasINEAssociationDespuesRenderer();

    private static ArrayList listViasINE = new ArrayList();
    private static ArrayList listViasCatastro = new ArrayList();
    private static ArrayList listViasCatastroAsociadas = new ArrayList();
    
    private static Connection con = null;
   
    
    private boolean erroresDeAsociacion=false;
    
    private DefaultListModel listEnlazadosModel = new DefaultListModel();
    
    DefaultListModel listViaIneModel = new DefaultListModel() {
    	
    	
        public void addElement(Object obj) 
        {
            /*for (int n=0;n<this.size();n++)
            {
                DatosViasINE currentViaIne = (DatosViasINE) getElementAt(n);
                DatosViasINE newViaIne = (DatosViasINE) obj;
                if(newViaIne.getNombreVia().trim().compareTo(currentViaIne.getNombreVia().trim())<0)
                {
                    add(n,obj);
                    return;
                }
                else{
                	System.out.println("Repetido:"+currentViaIne.getNombreOficial());
                }
            }*/
            //add(this.size(),obj);
            super.addElement(obj);
        }
    };
    
    DefaultListModel listCatastroModel = new DefaultListModel() {
        public void addElement(Object obj) 
        {
        	//Verificamos para no annadir 2 veces el mismo elemento.
        	 for (int n=0;n<this.size();n++)
             {
                 DatosViasCatastro currentViaCatastro = (DatosViasCatastro) getElementAt(n);
                 DatosViasCatastro newViaCatastro = (DatosViasCatastro) obj;

                 if (((newViaCatastro.getNombreCatastro()!=null) && (currentViaCatastro.getNombreCatastro()!=null)) 
                		 && ((!newViaCatastro.getNombreCatastro().trim().equals("")) && (!currentViaCatastro.getNombreCatastro().trim().equals(""))) 
                		 && (newViaCatastro.getNombreCatastro().trim().compareTo(currentViaCatastro.getNombreCatastro().trim())<0))
                 {
                     add(n,obj);
                     return;
                 }
             }
             super.addElement(obj);
        }
        
    };
    
    private JList lstViaINE = new JList(listViaIneModel);
    private JList lstCatastro = new JList(listCatastroModel);
    private JList lstEnlazados = new JList(listEnlazadosModel);
    
    public GeopistaAsociarCatastroINEViasDespuesPanel(){
    	jbInit();
    }

    /**
     * jbInit()
     * 
     * Inicia el Panel
     */
    private void jbInit(){
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.report(aplicacion.getI18nString("CargandoDatosIniciales"));
        progressDialog.addComponentListener(new ComponentAdapter(){
        	public void componentShown(ComponentEvent e){
        		new Thread(new Runnable(){
        			public void run(){
        				try{
        					btnEliminar.setEnabled(false);
        					lstViaINE.setCellRenderer(viasIneRenderer);
        					lstCatastro.setCellRenderer(viasCatastroRenderer);
        					lstEnlazados.setCellRenderer(viasCatastroViasINEAssociationRenderer);
                            lstViaINE.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            lstCatastro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            lstEnlazados.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                            setName(aplicacion.getI18nString("importar.asistente.callejero.titulo.5"));
                            setSize(new Dimension(800, 529));
                            setLayout(null);
                            pnlCatastro.setBounds(new Rectangle(140, 55, 260, 255));

                            pnlINE.setBounds(new Rectangle(540, 55, 270, 255));

                            lblCatastro.setBounds(new Rectangle(140, 10, 280, 20));
                            lblCatastro.setText(aplicacion.getI18nString("lblCatastroVias"));
                            pnlEnlazados.setBounds(new Rectangle(145, 335, 510, 170));

                            lstCatastro.setBounds(new Rectangle(0, 0, 264, 271));
                            lstViaINE.setBounds(new Rectangle(0, 0, 281, 271));
                            btnAsociar.setText(aplicacion.getI18nString("btnAsociar"));
                            btnAsociar.setBounds(new Rectangle(410, 155, 120, 30));
                            btnAsociar.addActionListener(new ActionListener(){
                            	public void actionPerformed(ActionEvent e){
                            		btnAsociar_actionPerformed(e);
                            	}
                            });
                            btnEliminar.setText(aplicacion.getI18nString("btnEliminar"));
                            btnEliminar.setBounds(new Rectangle(665, 420, 115, 35));
                            btnEliminar.addActionListener(new ActionListener(){
                            	public void actionPerformed(ActionEvent e){
                            		btnEliminar_actionPerformed(e);
                            	}
                            });

                            lblINEVias.setText(aplicacion.getI18nString("lblINEVias"));
                            lblINEVias.setBounds(new Rectangle(540, 10, 245, 20));
                            lblEnlazados.setText(aplicacion.getI18nString("lblEnlazados"));
                            lblEnlazados.setBounds(new Rectangle(140, 310, 285, 20));
                            lblErrores.setBounds(new Rectangle(495, 310, 280, 20));
                            lblErrores.setForeground(Color.red);
                            lblImagen.setIcon(IconLoader.icon("inf_referencia.png"));
                            lblImagen.setBounds(new Rectangle(15, 20, 110, 490));
                            lblImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                            lblAsociar.setBounds(new Rectangle(140, 20, 640, 30));

                            pnlCatastro.getViewport().add(lstCatastro, null);
                            add(lblAsociar, null);
                            add(lblImagen, null);
                            add(lblErrores, null);
                            add(lblEnlazados, null);
                            add(lblINEVias, null);
                            add(btnEliminar, null);
                            add(btnAsociar, null);
                            pnlEnlazados.getViewport().add(lstEnlazados, null);
                            add(pnlEnlazados, null);
                            add(lblCatastro, null);
                            pnlINE.getViewport().add(lstViaINE, null);
                            add(pnlINE, null);
                            add(pnlCatastro, null);
        				} catch (Exception e){

        				} finally{
        					progressDialog.setVisible(false);
        				}
        			}
        		}).start();
        	}
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    private void btnAsociar_actionPerformed(ActionEvent e){
        if ((lstCatastro.getSelectedIndex() != -1) && (lstViaINE.getSelectedIndex() != -1)){
            ViasCatastroViasINEDespuesAssociations newAssociation = new ViasCatastroViasINEDespuesAssociations(
            		(DatosViasCatastro) lstCatastro.getSelectedValue(),
                    (DatosViasINE) lstViaINE.getSelectedValue());

            listEnlazadosModel.addElement(newAssociation);
            listCatastroModel.remove(lstCatastro.getSelectedIndex());
            DatosViasINE viaINE=(DatosViasINE)lstViaINE.getSelectedValue();
            viaINE.setAsociada(true);
            

            btnEliminar.setEnabled(true);
        }
    }

    private void btnEliminar_actionPerformed(ActionEvent e){
        int[] selectedDeleteIndex = lstEnlazados.getSelectedIndices();
        Object[] selectedValues = lstEnlazados.getSelectedValues();

        for (int contadorLocal = selectedDeleteIndex.length - 1; contadorLocal >= 0; contadorLocal--){
            ViasCatastroViasINEDespuesAssociations currentAssociation = (ViasCatastroViasINEDespuesAssociations) selectedValues[contadorLocal];
            //listViaIneModel.addElement(currentAssociation.getDatosViasINE());
            listCatastroModel.addElement(currentAssociation.getDatosViasCatastro());
            listEnlazadosModel.remove(selectedDeleteIndex[contadorLocal]);
            
            DatosViasINE viaINE=currentAssociation.getDatosViasINE();
            viaINE.setAsociada(false);
        }
        lstViaINE.updateUI();

        if (listEnlazadosModel.getSize() == 0){
            btnEliminar.setEnabled(false);
        }
    }

    public void valueChanged(ListSelectionEvent e){

    }
    
    public void cargarDatosViasINE(){
        try{
        	int idMunicipio = AppContext.getIdMunicipio();
        	if (listViasINE == null)
        		listViasINE = new ArrayList();
        	else
        		listViasINE.clear();
        	
        	
        	listViaIneModel.removeAllElements();
        	
        	
            PreparedStatement  ps = null;
            ResultSet r = null;
            // selectViasIne = select id_via, nombrecortoine, nombreine, posiciontipovia, tipovia, codigoviaine, id_municipio from viasine where id_municipio = ? ORDER BY nombreine
            ps = con.prepareStatement("selectViasIne2");
            ps.setInt(1,idMunicipio);
            r=ps.executeQuery();
            while (r.next()){
            	DatosViasINE viasine=new DatosViasINE();
            	viasine.setIdMunicipio(r.getString("id_municipio"));
            	viasine.setIdVia(r.getString("id_via"));
            	viasine.setNombreCorto(r.getString("nombrecortoine"));
            	viasine.setNombreVia(r.getString("nombreine"));
            	viasine.setPosicionVia(r.getString("posiciontipovia"));
            	viasine.setTipoVia(r.getString("tipovia"));
            	viasine.setCodigoViaINE(r.getString("codigoviaine"));
            	viasine.setNombreOficial(r.getString("nombreoficial"));
            	viasine.setNormalized(GeopistaUtil.removeSymbols(r.getString("nombreine")));
            	
            	// Comprobar que es CodigoViaINE en vez de id_via:
            	if ((viasine.getCodigoViaINE() != null) && !(viasine.getCodigoViaINE().trim().equals(""))){
            		listViaIneModel.addElement(viasine);
            	}
            	//System.out.println("Via:"+r.getString("nombreine"));
            	
                listViasINE.add(viasine);
                
            }
            r.close();
            ps.close();
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        
    }
    
    public void cargarDatosViasCatastro(){
        try{         
            int idMunicipio = AppContext.getIdMunicipio();
           
            if (listViasCatastro == null)
            	listViasCatastro = new ArrayList();
            else
            	listViasCatastro.clear();
            
            if (listViasCatastroAsociadas == null)
            	listViasCatastroAsociadas = new ArrayList();
            else
            	listViasCatastroAsociadas.clear();
            
            listEnlazadosModel.removeAllElements();
            listCatastroModel.removeAllElements();

            PreparedStatement  ps = null;
            ResultSet r = null;
            /*
             * selectViasCatastro = SELECT nombrecatastro, id, codigoine, nombreviaine, posiciontipovia, tipoviaine FROM vias where nombrecatastro != '' and id_municipio = ? ORDER BY nombrecatastro
             * */
            ps = con.prepareStatement("selectViasCatastro");
            ps.setInt(1,idMunicipio);
            r=ps.executeQuery();
            while (r.next()){
                DatosViasCatastro viascatastro = new DatosViasCatastro();
                viascatastro.setNombreCatastro(r.getString("nombrecatastro"));
                viascatastro.setIdVia(r.getString("id"));
                viascatastro.setCodigoIne(r.getString("codigoine"));
                viascatastro.setNombreViaIne(r.getString("nombreviaine"));
                viascatastro.setPosicionTipoViaIne(r.getString("posiciontipovia"));
                viascatastro.setTipoViaIne(r.getString("tipoviaine"));
                
                viascatastro.setNormalized(GeopistaUtil.removeSymbols(r.getString("nombrecatastro")));
                
                if ((viascatastro.getCodigoIne() == null) || viascatastro.getCodigoIne().trim().equals("")){
                	//Si no estan casadas, intentamos hacer un matching
                	DatosViasINE currentViaINE=this.specialMatch(viascatastro);
                	if (currentViaINE==null)
                		listCatastroModel.addElement(viascatastro);
                	else{
                		//Si el numero de coincidencias es >1 la mostramos en rojo.
                		if (viascatastro.getIndiceCoincidencias().size()>1){
                			listCatastroModel.addElement(viascatastro);
                			erroresDeAsociacion=true;
                		}
                		else{	
                			viascatastro.setCodigoIne(currentViaINE.getCodigoViaINE());
                			listViasCatastroAsociadas.add(viascatastro);
                			currentViaINE.setAsociada(true);
                			//lstViaINE.doLayout();
                		}
                	}
                }
                else{
                	//Buscamos cual es la via del INE asociada.
                	Iterator listViasINEIterator = listViasINE.iterator();
                	while (listViasINEIterator.hasNext()){
                		DatosViasINE currentViaINE = (DatosViasINE) listViasINEIterator.next();
                		if (currentViaINE.getCodigoViaINE().equals(viascatastro.getCodigoIne())){
                			currentViaINE.setAsociada(true);
                			break;
                		}
                	}                	
                	viascatastro.setInicialmenteAsociada(true);
                	listViasCatastroAsociadas.add(viascatastro);
                }
            
                listViasCatastro.add(viascatastro);
                
            }
            r.close();
            ps.close();            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    private DatosViasINE specialMatch(DatosViasCatastro viascatastro){
    	

    	DatosViasINE viaEncontrada=null;
    	ArrayList indiceCoincidencias=new ArrayList();
    	
    	Iterator listViasINEIterator = listViasINE.iterator();
    	int indice=0;
    	while (listViasINEIterator.hasNext()){
    		DatosViasINE currentViaINE = (DatosViasINE) listViasINEIterator.next();
 
	    	String cadena1=viascatastro.getNormalized();
	    	String cadena2=currentViaINE.getNormalized();
	    	if (GeopistaUtil.compareSpecial(cadena1,cadena2)){
	    		viaEncontrada=currentViaINE;
	    		indiceCoincidencias.add(indice);
	    	}
	    	indice++;
    	}
    	viascatastro.setIndiceCoincidencias(indiceCoincidencias);
    	if (indiceCoincidencias.size()>0)
    		return viaEncontrada;
    	else
    		return null;
    }
    
    
    public void asociarDatosViasCatastroINE(){
    	Iterator listViasCatastroAsociadasIterator = listViasCatastroAsociadas.iterator();
    	while (listViasCatastroAsociadasIterator.hasNext()){
    		DatosViasCatastro currentViaCatastro = (DatosViasCatastro) listViasCatastroAsociadasIterator.next();
    		Iterator listViasINEIterator = listViasINE.iterator();
    		boolean encontrado = false;
            while ((listViasINEIterator.hasNext()) && (!encontrado)){
            	DatosViasINE currentViaINE = (DatosViasINE) listViasINEIterator.next();
            	if (currentViaCatastro.getCodigoIne().equals(currentViaINE.getCodigoViaINE())){
            		encontrado = true;
            		ViasCatastroViasINEDespuesAssociations newAssociation = new ViasCatastroViasINEDespuesAssociations(
            				currentViaCatastro, currentViaINE);

                    listEnlazadosModel.addElement(newAssociation);
                    btnEliminar.setEnabled(true);
            	}
            }
    	}
    }
    
    public void enteredFromLeft(Map dataMap) {
        //wizardContext.previousEnabled(false);

        try{
            if (con == null){
                con = aplicacion.getConnection();
                con.setAutoCommit(false);
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion.getI18nString("NoConexionBaseDatos"));
            wizardContext.cancelWizard();
            return;
        }
        
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion
                .getMainFrame(), null);

        progressDialog.setTitle(aplicacion
                .getI18nString("Inforeferencia.GuardarCambios5"));
        progressDialog.report(aplicacion
                .getI18nString("Inforeferencia.GuardarCambios5"));
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
                                	//Meter Codigo
                                	cargarDatos();
                                	//Fin Codigo

                                } catch (Exception e)
                                {
                                    logger.error("Error",e);
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
        
        if (erroresDeAsociacion)
            JOptionPane.showMessageDialog(aplicacion.getMainFrame(), aplicacion
                    .getI18nString("Inforeferencia.GuardarCambios6"));        		

        
		//cargarDatos();
    }
    
    public void cargarDatos() {

        cargarDatosViasINE();
        cargarDatosViasCatastro();
        asociarDatosViasCatastroINE(); 
             
        MouseListener[] listeners=lstCatastro.getMouseListeners();
        for (int i=0;i<listeners.length;i++){
        	MouseListener listener=(MouseListener)listeners[i];
        	lstCatastro.removeMouseListener(listener);
        }
        
        lstCatastro.addMouseListener(new MouseAdapter() {
        		public void mouseClicked(MouseEvent me) {
        			if (me.getClickCount() == 2) {
	        			Point p = new Point(me.getX(),me.getY());
	        			lstCatastro.setSelectedIndex(lstCatastro.locationToIndex(p));
	        			DatosViasCatastro datosViaCatastro=(DatosViasCatastro)lstCatastro.getModel().getElementAt(lstCatastro.locationToIndex(p));
	        			if (datosViaCatastro.getIndiceCoincidencias().size()>0){
	        				ArrayList coincidencias=datosViaCatastro.getIndiceCoincidencias();
	        				StringBuffer cadena=new StringBuffer();
	        				for (int i=0;i<coincidencias.size();i++){
	        					DatosViasINE datosViasINE=(DatosViasINE)listViaIneModel.get((Integer)coincidencias.get(i));
	        			        String texto=datosViasINE.getNombreVia().trim()+" ("+datosViasINE.getTipoVia().trim() +") -->["+datosViasINE.getNombreOficial() +"]\n";
	        					cadena.append(texto);
	        				}
	        				JOptionPane.showMessageDialog(aplicacion.getMainFrame(), 
	            					"Coincidencias con:\n"+cadena);  
	        				//popup.show(me.getComponent(), me.getX(), me.getY());
	        			}
	        			
        			}    
        			else{
        				Point p = new Point(me.getX(),me.getY());
        				lstCatastro.setSelectedIndex(lstCatastro.locationToIndex(p));
        			}
        		}
        	});

    }
    
    /**
     * Called when the user presses Next on this panel
     */
    public void exitingToRight() throws Exception{
        final TaskMonitorDialog progressDialog = new TaskMonitorDialog(aplicacion.getMainFrame(), null);

        progressDialog.setTitle(aplicacion.getI18nString("RealizadoAsociaciones"));
        progressDialog.report(aplicacion.getI18nString("RealizadoAsociaciones"));
        progressDialog.addComponentListener(new ComponentAdapter(){
        	public void componentShown(ComponentEvent e){
        		new Thread(new Runnable(){
        			public void run(){
        				try{
        					int idMunicipio = AppContext.getIdMunicipio();
        					eliminarAsociaciones(idMunicipio);
                            hacerAsociaciones(idMunicipio);
        				} catch (Exception e){
        					e.printStackTrace();
        				} finally{
        					progressDialog.setVisible(false);
        				}
        			}
        		}).start();
        	}
        });
        GUIUtil.centreOnWindow(progressDialog);
        progressDialog.setVisible(true);
    }

    private void eliminarAsociaciones(int idMunicipio) throws SQLException{
    	PreparedStatement ps = null;
    	
    	// Eliminar asociaciones viasine con vias:
    	// viasIneeliminarAsociaciones = update viasine set id_via = null where id_municipio = ?
    	ps = con.prepareStatement("viasIneeliminarAsociaciones");
        ps.setInt(1, idMunicipio);
        ps.executeUpdate();
        
        // Eliminar asociaciones vias con viasine:
        // viasCatastroeliminarAsociaciones = update vias set codigoine = null where id_municipio = ?
        ps = con.prepareStatement("viasCatastroeliminarAsociaciones");
        ps.setInt(1, idMunicipio);
        ps.executeUpdate();
    }
    
    private void hacerAsociaciones(int idMunicipio) throws SQLException{
        PreparedStatement ps = null;
        
        Enumeration elementsEnumerations = listEnlazadosModel.elements();

        while (elementsEnumerations.hasMoreElements()){
        	ViasCatastroViasINEDespuesAssociations viasCatastroINEAssociations = 
        		(ViasCatastroViasINEDespuesAssociations) elementsEnumerations.nextElement();

            // Obtenemos los valores

            DatosViasINE datosViasIne = (DatosViasINE) viasCatastroINEAssociations.getDatosViasINE();
            DatosViasCatastro datosViasCatastro = (DatosViasCatastro) viasCatastroINEAssociations.getDatosViasCatastro();

            try{
            	// Actualizar viasine:
            	// viasIneActualizar = update viasine set id_via = ? where codigoviaine = ? and id_municipio = ?
                ps = con.prepareStatement("viasIneActualizar");
                ps.setInt(1, Integer.parseInt(datosViasCatastro.getIdVia()));
                ps.setInt(2, Integer.parseInt(datosViasIne.getCodigoViaINE()));
                ps.setInt(3, idMunicipio);
                ps.executeUpdate();
                
                // Actualizar vias:
                // viasCatastroActualizar2 = UPDATE vias SET codigoine = ?,nombreviaine=?, nombreviacortoine=? WHERE id = ? AND id_municipio = ?
                ps = con.prepareStatement("viasCatastroActualizar2");
                ps.setInt(1, Integer.parseInt(datosViasIne.getCodigoViaINE()));
                ps.setString(2, datosViasIne.getNombreVia());
                ps.setString(3, datosViasIne.getNombreCorto());
                ps.setInt(4, Integer.parseInt(datosViasCatastro.getIdVia()));
                ps.setInt(5, idMunicipio);
                ps.executeUpdate();
            } catch (Exception e){
                e.printStackTrace();
                // si ocurre algun problema en una de la iteraciones
                // seguimos con la siguiente
            } finally{

                ps.close();
                con.commit();
            }
        }
    }

    /**
     * Tip: Delegate to an InputChangedFirer.
     * 
     * @param listener
     *            a party to notify when the input changes (usually the
     *            WizardDialog, which needs to know when to update the enabled
     *            state of the buttons.
     */
    public void add(InputChangedListener listener)
    {

    }

    public void remove(InputChangedListener listener)
    {

    }

    public String getTitle()
    {
        return this.getName();
    }

    public String getID()
    {
        return "5";
    }

    public String getInstructions()
    {
        return "";
    }

    public boolean isInputValid()
    {
        return true;
    }

    public void setNextID(String nextID)
    {
        this.nextID = nextID;
    }

    /**
     * @return null to turn the Next button into a Finish button
     */
    public String getNextID(){

        return nextID;
    }

    public void setWizardContext(WizardContext wd){
        this.wizardContext = wd;
    }

    /* (non-Javadoc)
     * @see com.geopista.ui.wizard.WizardPanel#exiting()
     */
    public void exiting(){
        listViasINE = null;
        listViasCatastro = null;
        listViasCatastroAsociadas = null;
        
    }
    
}