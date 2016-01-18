package com.geopista.ui.plugin.infcatfisicoeconomico.dialogs;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.intercambio.edicion.dialogs.GestionExpedientePanel;
import com.geopista.app.catastro.intercambio.edicion.dialogs.ImagenInterface;
import com.geopista.app.catastro.intercambio.edicion.dialogs.PintarImagenCatastro;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionOperations;
import com.geopista.app.catastro.intercambio.edicion.utils.EdicionUtils;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.app.catastro.registroExpedientes.utils.ConstantesRegistro;
import com.geopista.server.administradorCartografia.ACException;
import com.geopista.util.FeatureExtendedPanel;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.ui.GUIUtil;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorDialog;

public class ImagenCatastroPanel extends JPanel implements FeatureExtendedPanel, ImagenInterface
{
	private static final Log logger = LogFactory.getLog(ImagenCatastroPanel.class);
	
	private GestionExpedientePanel gestionExpedientePanel = null;
	
	final static int NEXT = 0;
    final static int PREVIOUS = 1;
	
	private JPanel jPanelDatosImagenes = null;
	private JPanel jPanelBotoneraImagenes = null;
	private JPanel jPanelThumbnails = null;
	private JPanel jPanelMostrarImagen = null;
	private JPanel jPanelDatos = null;
	private JButton jButtonImagenMenos = null;
	private JButton jButtonImagenMas = null;
	private JPanel jPanelBotoneraImagen = null;
	private JLabel jLabelNombre = null;
	private JLabel jLabelExtension = null;
	private JLabel jLabelTipoDocumento = null;	
	private JTextField jTextFieldNombre = null;
	private JTextField jTextFieldExtension = null;
	private JTextField jTextFieldTipoDocumento = null;
	private JButton jButtonThumbnailMas = null;
	private JButton jButtonThumbnailMenos = null;
	private JPanel jPanelBotoneraThumbnail = null;
	private JPanel jPanelImagenes = null;	
    private JPanel jPanelDatosImagen = null;
	
	private PintarImagenCatastro jPanelPintarImagenCatastro = null;
	private PintarImagenCatastro jPanelPintarImagenCatastro1 = null;
	private PintarImagenCatastro jPanelPintarImagenCatastro2 = null;
	private PintarImagenCatastro jPanelPintarImagenCatastro3 = null;
	private PintarImagenCatastro jPanelPintarImagenCatastro4 = null;
	private PintarImagenCatastro jPanelPintarImagenCatastro5 = null;
	
	public ArrayList lstImagenes = new ArrayList();
	private Vector jPanelImgThumbnails = new Vector();
	
	private int indiceImagenSeleccionada;
	int imagenInicialPanel = 0;
    int imagenFinalPanel = 4;
    
    private ImagenCatastro imageSelected = null;

	private JPanel jPanelImagenPrincipal = null;
	
	private String refCatastral = null;
	private String lastRefCatastral = "";
    
    
    private PintarImagenCatastro getJPanelPintarImagenCatastro(ImagenInterface imagenInterface){
    	
    	if (jPanelPintarImagenCatastro == null){
    		
    		jPanelPintarImagenCatastro = new PintarImagenCatastro(imagenInterface);
    		
    	}
    	return jPanelPintarImagenCatastro;
    }
    
    private PintarImagenCatastro getJPanelPintarImagenCatastro1(ImagenInterface imagenInterface){
    	
    	if (jPanelPintarImagenCatastro1 == null){
    		
    		jPanelPintarImagenCatastro1 = new PintarImagenCatastro(imagenInterface);
    		jPanelImgThumbnails.add(jPanelPintarImagenCatastro1);
    		
    	}
    	return jPanelPintarImagenCatastro1;
    }
    
    private PintarImagenCatastro getJPanelPintarImagenCatastro2(ImagenInterface imagenInterface){
    	
    	if (jPanelPintarImagenCatastro2 == null){
    		
    		jPanelPintarImagenCatastro2 = new PintarImagenCatastro(imagenInterface);
    		jPanelImgThumbnails.add(jPanelPintarImagenCatastro2);
    		
    	}
    	return jPanelPintarImagenCatastro2;
    }
	
    private PintarImagenCatastro getJPanelPintarImagenCatastro3(ImagenInterface imagenInterface){
    	
    	if (jPanelPintarImagenCatastro3 == null){
    		
    		jPanelPintarImagenCatastro3 = new PintarImagenCatastro(imagenInterface);
    		jPanelImgThumbnails.add(jPanelPintarImagenCatastro3);
    		
    	}
    	return jPanelPintarImagenCatastro3;
    }
    
    private PintarImagenCatastro getJPanelPintarImagenCatastro4(ImagenInterface imagenInterface){
    	
    	if (jPanelPintarImagenCatastro4 == null){
    		
    		jPanelPintarImagenCatastro4 = new PintarImagenCatastro(imagenInterface);
    		jPanelImgThumbnails.add(jPanelPintarImagenCatastro4);
    		
    	}
    	return jPanelPintarImagenCatastro4;
    }
    
    private PintarImagenCatastro getJPanelPintarImagenCatastro5(ImagenInterface imagenInterface){
    	
    	if (jPanelPintarImagenCatastro5 == null){
    		
    		jPanelPintarImagenCatastro5 = new PintarImagenCatastro(imagenInterface);
    		jPanelImgThumbnails.add(jPanelPintarImagenCatastro5);
    		
    	}
    	return jPanelPintarImagenCatastro5;
    }
		
	private JPanel getJPanelDatosImagenes() {
		
		if (jPanelDatosImagenes == null){
			
			jPanelDatosImagenes = new JPanel();
			jPanelDatosImagenes.setLayout(new GridBagLayout());
			
			//Imágenes Asociadas
			jPanelDatosImagenes.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("InfCatastralFisicoEconomicoPlugIn", "infCatastralFisicoEconomico.img.panel.title"), TitledBorder.LEADING, 
                    		TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
			
			jPanelDatosImagenes.add(getJPanelMostrarImagen(), new GridBagConstraints(0,0,1,1, 0.7, 0.7,
                    GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
			
			jPanelDatosImagenes.add(getJPanelThumbnails(), new GridBagConstraints(0,1,1,1, 0.3, 0.3,
                    GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
			
		}
		return jPanelDatosImagenes;
	}
	
	private JPanel getJPanelDatosImagen(){
		
		if (jPanelDatosImagen == null){
			
			jPanelDatosImagen  = new JPanel();
			jPanelDatosImagen.setLayout(new GridBagLayout());
			
			jPanelDatosImagen.setMaximumSize(this.getSize());
			jPanelDatosImagen.setMinimumSize(this.getSize());
			
			jPanelDatosImagen.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
						
			jPanelDatosImagen.add(getJPanelDatos(), new GridBagConstraints(0,0,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(5,5,5,5),0,0));
			
			jPanelDatosImagen.add(getJPanelBotoneraImagenes(), new GridBagConstraints(0,1,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(5,5,5,5),0,0));
			
		}
		return jPanelDatosImagen;
	}
		
	private JPanel getJPanelDatos(){
		
		if (jPanelDatos == null){
		
			jPanelDatos = new JPanel();
			jPanelDatos.setLayout(new GridBagLayout());
			
			jLabelNombre = new JLabel("", JLabel.CENTER); 
            jLabelNombre.setText(I18N.get("InfCatastralFisicoEconomicoPlugIn", "infCatastralFisicoEconomico.img.panel.attribute.name")); 
            
            jLabelExtension = new JLabel("", JLabel.CENTER); 
            jLabelExtension.setText(I18N.get("InfCatastralFisicoEconomicoPlugIn", "infCatastralFisicoEconomico.img.panel.attribute.ext")); 
            
            jLabelTipoDocumento = new JLabel("", JLabel.CENTER); 
            jLabelTipoDocumento.setText(I18N.get("InfCatastralFisicoEconomicoPlugIn", "infCatastralFisicoEconomico.img.panel.attribute.type")); 
            
            jPanelDatos.add(jLabelNombre, 
                    new GridBagConstraints(0,0,1,1,0.1, 0.1,GridBagConstraints.EAST, 
                    		GridBagConstraints.NONE, new Insets(0,15,0,15),0,0));
            
            jPanelDatos.add(getJTextFieldNombre(), 
                    new GridBagConstraints(1,0,2,1,0.7, 0.1,GridBagConstraints.WEST, 
                    		GridBagConstraints.NONE, new Insets(0,25,0,25),0,0));
            
            jPanelDatos.add(jLabelExtension, 
                    new GridBagConstraints(0,1,1,1,0.1, 0.1,GridBagConstraints.EAST, 
                    		GridBagConstraints.NONE, new Insets(0,15,0,15),0,0));
            
            jPanelDatos.add(getJTextFieldExtension(), 
                    new GridBagConstraints(1,1,2,1,0.7, 0.1,GridBagConstraints.WEST, 
                    		GridBagConstraints.NONE, new Insets(0,25,0,25),0,0));
            
            jPanelDatos.add(jLabelTipoDocumento, 
                    new GridBagConstraints(0,2,1,1,0.1, 0.1,GridBagConstraints.EAST, 
                    		GridBagConstraints.NONE, new Insets(0,15,0,15),0,0));
            
            jPanelDatos.add(getJTextFieldTipoDocumento(), 
                    new GridBagConstraints(1,2,2,1,0.7, 0.1,GridBagConstraints.WEST, 
                    		GridBagConstraints.NONE, new Insets(0,25,0,25),0,0));
                                    
		}
		return jPanelDatos;
	}
	
	private JTextField getJTextFieldNombre(){
		
		if (jTextFieldNombre == null){
			
			jTextFieldNombre  = new JTextField(25);
			
		}
		return jTextFieldNombre;
	}
	
	private JTextField getJTextFieldExtension(){
		
		if (jTextFieldExtension == null){
			
			jTextFieldExtension  = new JTextField(10);
			
		}
		return jTextFieldExtension;
	}
	
	
	private JTextField getJTextFieldTipoDocumento(){
		
		if (jTextFieldTipoDocumento == null){
			
			jTextFieldTipoDocumento    = new JTextField(25);
			
		}
		return jTextFieldTipoDocumento;
	}
	
	
	private JPanel getJPanelMostrarImagen(){
		
		if (jPanelMostrarImagen == null){
			
			jPanelMostrarImagen = new JPanel();			
			jPanelMostrarImagen.setLayout(new GridBagLayout());
			//Imagen
			jPanelMostrarImagen.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("InfCatastralFisicoEconomicoPlugIn", "infCatastralFisicoEconomico.img.panel.imagen"), TitledBorder.LEADING, 
                    		TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
			
			jPanelMostrarImagen.add(getJPanelImagenPrincipal(), new GridBagConstraints(0,0,1,1, 0.9, 0.9,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
			
			jPanelMostrarImagen.add(getJPanelBotoneraImagen(), new GridBagConstraints(0,1,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(5,5,5,5),0,0));
			
			
		}
		return jPanelMostrarImagen;
	}
	
	private JPanel getJPanelImagenPrincipal(){
		
		if (jPanelImagenPrincipal == null){
			
			jPanelImagenPrincipal  = new JPanel();
			jPanelImagenPrincipal.setLayout(new GridBagLayout());
			
			jPanelImagenPrincipal.add(getJPanelPintarImagenCatastro(this), new GridBagConstraints(0,0,1,1, 0.7, 0.7,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
			
			jPanelImagenPrincipal.add(getJPanelDatosImagen(), new GridBagConstraints(1,0,1,1, 0.3, 0.3,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
			
			
		}
		return jPanelImagenPrincipal;
	}
	
	private JPanel getJPanelBotoneraImagen(){
		
		if (jPanelBotoneraImagen  == null){
			
			jPanelBotoneraImagen = new JPanel();
			jPanelBotoneraImagen.setLayout(new GridBagLayout());
			
			jPanelBotoneraImagen.add(getJButtonImagenMenos(), new GridBagConstraints(0,0,1,1, 0.1, 0.1,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, 
                    new Insets(0,5,0,5),0,0));
			
			jPanelBotoneraImagen.add(getJButtonImagenMas(), new GridBagConstraints(1,0,1,1, 0.1, 0.1,
                    GridBagConstraints.WEST, GridBagConstraints. NONE, 
                    new Insets(0,5,0,5),0,0));
		}
		return jPanelBotoneraImagen;
	}
	
	private JPanel getJPanelBotoneraThumbnail(){
		
		if (jPanelBotoneraThumbnail  == null){
			
			jPanelBotoneraThumbnail  = new JPanel();
			jPanelBotoneraThumbnail.setLayout(new GridBagLayout());
			
			jPanelBotoneraThumbnail.add(getJButtonThumbnailMenos(), new GridBagConstraints(0,0,1,1, 0.1, 0.1,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, 
                    new Insets(0,5,0,5),0,0));
			
			jPanelBotoneraThumbnail.add(getJButtonThumbnailMas(), new GridBagConstraints(1,0,1,1, 0.1, 0.1,
                    GridBagConstraints.EAST, GridBagConstraints. NONE, 
                    new Insets(0,5,0,5),0,0));
		}
		return jPanelBotoneraThumbnail;
	}
	
	private JButton getJButtonImagenMenos(){
		
		if (jButtonImagenMenos == null){
			
			jButtonImagenMenos = new JButton();
			jButtonImagenMenos.setText("<<");
			jButtonImagenMenos.addActionListener(new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                desplazarImagen(PREVIOUS);
	            }
	        });
		}
		return jButtonImagenMenos;
	}
	
	private JButton getJButtonImagenMas(){
		
		if (jButtonImagenMas == null){
			
			jButtonImagenMas = new JButton();
			jButtonImagenMas.setText(">>");
	        jButtonImagenMas.addActionListener(new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                desplazarImagen(NEXT);
	            }
	        });
		}
		return jButtonImagenMas;
	}
	
	private JButton getJButtonThumbnailMas(){
		
		if (jButtonThumbnailMas == null){
			
			jButtonThumbnailMas  = new JButton();
			jButtonThumbnailMas.setText(">>");
	        jButtonThumbnailMas.addActionListener(new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                desplazarImagenes(NEXT);
	            }
	        });
		}
		return jButtonThumbnailMas;
	}
	
	private JButton getJButtonThumbnailMenos(){
		
		if (jButtonThumbnailMenos == null){
			
			jButtonThumbnailMenos   = new JButton();
			jButtonThumbnailMenos.setText("<<");
			jButtonThumbnailMenos.addActionListener(new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                desplazarImagenes(PREVIOUS);
	            }
	        });
		}
		return jButtonThumbnailMenos;
	}

	private JPanel getJPanelBotoneraImagenes() {
		
		if (jPanelBotoneraImagenes == null){
			
			jPanelBotoneraImagenes = new JPanel();
			jPanelBotoneraImagenes.setLayout(new GridBagLayout());
			
			
						
		}
		return jPanelBotoneraImagenes;
	}
	
	private JPanel getJPanelThumbnails() {
		
		if (jPanelThumbnails == null){
			
			jPanelThumbnails = new JPanel();
			jPanelThumbnails.setLayout(new GridBagLayout());
			
			jPanelThumbnails.setBorder(BorderFactory.createTitledBorder
                    (null, I18N.get("InfCatastralFisicoEconomicoPlugIn", "infCatastralFisicoEconomico.img.panel.thumbnail.title"), TitledBorder.LEADING, 
                    		TitledBorder.TOP, new Font(null, Font.BOLD, 12))); 
			
	        jPanelThumbnails.add(getJPanelImagenes(), new GridBagConstraints(0,0,1,1, 0.9, 0.9,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
	        	        	        
	        jPanelThumbnails.add(getJPanelBotoneraThumbnail(), new GridBagConstraints(0,1,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
                    new Insets(0,5,0,5),0,0));
	        	        
		}
		return jPanelThumbnails;
	}
	
	private JPanel getJPanelImagenes(){
		
		if (jPanelImagenes == null){
			
			jPanelImagenes  = new JPanel();
			jPanelImagenes.setLayout(new GridBagLayout());
			
			jPanelImagenes.add(getJPanelPintarImagenCatastro1(this), new GridBagConstraints(0,0,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));

			jPanelImagenes.add(getJPanelPintarImagenCatastro2(this), new GridBagConstraints(1,0,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));

			jPanelImagenes.add(getJPanelPintarImagenCatastro3(this), new GridBagConstraints(2,0,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));

			jPanelImagenes.add(getJPanelPintarImagenCatastro4(this), new GridBagConstraints(3,0,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));

			jPanelImagenes.add(getJPanelPintarImagenCatastro5(this), new GridBagConstraints(4,0,1,1, 0.1, 0.1,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
                    new Insets(0,5,0,5),0,0));
			
		}
		return jPanelImagenes;
	}

	/**
     * This method initializes 
     * 
     */
    public ImagenCatastroPanel() {
    	super();
    	initialize();
    }
    
    public ImagenCatastroPanel(GestionExpedientePanel gestionExpedientePanel) {
    	super();
    	this.gestionExpedientePanel = gestionExpedientePanel;
    	initialize();
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
    	
        this.setLayout(new GridBagLayout());        
//        this.setSize(new java.awt.Dimension(800,575));
//        this.setPreferredSize(new java.awt.Dimension(800,575));
//        this.setMaximumSize(this.getPreferredSize());
//        this.setMinimumSize(this.getPreferredSize());
                
        this.add(getJPanelDatosImagenes(), new GridBagConstraints(0,0,1,1, 1, 1,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, 
                new Insets(0,5,0,5),0,0));
		        
        indiceImagenSeleccionada=-1;
        habilitarBotones();
        getJTextFieldNombre().setEditable(false);
        getJTextFieldExtension().setEditable(false);
        getJTextFieldTipoDocumento().setEditable(false);
       
        this.updateUI();
    		
    }

    public void enter()
    {
    	final String ref_Catastral = this.refCatastral;
        
    	if (this.gestionExpedientePanel != null){
        	this.gestionExpedientePanel.getJButtonValidar().setEnabled(false);
        	
        	if(this.gestionExpedientePanel.getExpediente().getIdEstado() >= ConstantesRegistro.ESTADO_FINALIZADO)
            {
                EdicionUtils.enablePanel(this.gestionExpedientePanel.getJPanelBotones(), false);                
            }
        }
    	
    	final TaskMonitorDialog progressDialog = new TaskMonitorDialog(AppContext.getApplicationContext().getMainFrame(), null);
        progressDialog.setTitle("TaskMonitorDialog.Wait");
        progressDialog.report(I18N.get("InfCatastralFisicoEconomicoPlugIn","infCatastralFisicoEconomico.img.panel.loadimages"));
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
                            EdicionOperations oper = new EdicionOperations();
                            ArrayList lstImg = null;
                            if (ref_Catastral != null && !refCatastral.equals(lastRefCatastral)){
                            	//lstImg = oper.obtenerLstImagenes(id_Expediente,ref_Catastral);
                            	lstImg = oper.buscarImagenesFinca(ref_Catastral);
                            	setImagenes(null);
                            	lastRefCatastral = ref_Catastral;
                            }
                            
                            if(lstImagenes==null && lstImg != null){
                            	
                            	cleanImagenes();
                            	for (Iterator iterLstImagenes = lstImg.iterator();iterLstImagenes.hasNext();){
                            		
                            		ImagenCatastro imagen = (ImagenCatastro)iterLstImagenes.next();
                            		if (lstImagenes == null){
                            			lstImagenes = new ArrayList();
                        			}
                            		lstImagenes.add(imagen);		
                        			
                        			int index=lstImagenes.size();
                        	        if(imagenFinalPanel<(index-1))
                        	        {
                        	           imagenInicialPanel=index-jPanelImgThumbnails.size();
                        	           imagenFinalPanel= index-1;
                        	        }
                        	        setImageSelected(imagen);
                	                indiceImagenSeleccionada++;
                	                pintarImagen(imagen);
                	                load(imagen);
                	                actualizarModelo();
                        	        
                            	}

                            }
//                            else{
//                            	cleanImagenes();
//                            }
 
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

    	habilitarBotones();
       
    }

    public void exit()
    {   
    }
    
    public void guardarImagen(ImagenCatastro imagen)
    {
    
    }
    
    public void guardarImagenNoSeleccionado(ImagenCatastro imagen)
    {
    
    }


	public void setImagenPanel(ImagenCatastroPanel imagenCatastroPanel) {

		
	}
	
	public void setLstImagenes(String refCat){
        this.refCatastral = refCat;
	}
	
	private void setImagenes(ArrayList imagenes){
		this.lstImagenes = imagenes;
	}
	
	public ArrayList getLstImagenes() {

		return lstImagenes;
		
	}

	public void cleanup() {
		
	  jTextFieldExtension = null;
	  jTextFieldNombre = null;
	  jTextFieldTipoDocumento = null;
	  setImagenes(null);
	  cleanImagenes();
	  
	}
	
	private void cleanImagenes(){
		
		try{
			  getJPanelPintarImagenCatastro(this).setImagen(null);
			  getJPanelPintarImagenCatastro(this).setSelected(false);
			  getJPanelPintarImagenCatastro1(this).setImagen(null);
			  getJPanelPintarImagenCatastro1(this).setSelected(false);
			  getJPanelPintarImagenCatastro2(this).setImagen(null);
			  getJPanelPintarImagenCatastro2(this).setSelected(false);
			  getJPanelPintarImagenCatastro3(this).setImagen(null);
			  getJPanelPintarImagenCatastro3(this).setSelected(false);
			  getJPanelPintarImagenCatastro4(this).setImagen(null);
			  getJPanelPintarImagenCatastro4(this).setSelected(false);
			  getJPanelPintarImagenCatastro5(this).setImagen(null);
			  getJPanelPintarImagenCatastro5(this).setSelected(false);
			  indiceImagenSeleccionada = -1;
		  }
		  catch (Exception e) {
			  e.printStackTrace();
		  }
	}

	private void desplazarImagen(int direccion)
    {
        try
        {
            if (lstImagenes == null) return;
            
            saveImagen();
                        
            if (direccion == NEXT)
            {
                indiceImagenSeleccionada++;
                if (indiceImagenSeleccionada>imagenFinalPanel)
                    desplazarImagenes(NEXT);
            }
            else
            {
                indiceImagenSeleccionada--;
                if (indiceImagenSeleccionada<imagenInicialPanel)
                    desplazarImagenes(PREVIOUS);
            }
            if (lstImagenes.size()>indiceImagenSeleccionada){
            	ImagenCatastro imagen = (ImagenCatastro) lstImagenes.get(indiceImagenSeleccionada);
            	setImageSelected(imagen);
            	load(imagen);
            	pintarImagen(imagen);
            	actualizarModelo();
            	getJTextFieldNombre().setEditable(true);
            	getJTextFieldExtension().setEditable(true);
            	getJTextFieldTipoDocumento().setEditable(true);
            }
//            seleccionar(imagen, indiceImagenSeleccionada);
        }
        catch (Exception e)
        {
            logger.error("Error al pulsar los botones de desplazamiento ", e);
        }
    }
	
	private void desplazarImagenes(int direccion)
    {
        try
        {
            if (lstImagenes == null) return;
            if (direccion == NEXT)
            {
                imagenInicialPanel++;
                imagenFinalPanel++;
            }
            else
            {
                imagenInicialPanel--;
                imagenFinalPanel--;
            }
            actualizarModelo();
        }
        catch (Exception e)
        {
            logger.error("Error al pulsar los botones de desplazamiento ", e);
        }
    }
	
	public void seleccionar(ImagenCatastro imagen, int indicePanel)
    {
        try
        {
        	saveImagen();
            setImageSelected(imagen);
            load(imagen);
            pintarImagen(imagen);
            this.indiceImagenSeleccionada = indicePanel;
            actualizarModelo();
            getJTextFieldNombre().setEditable(true);
            getJTextFieldExtension().setEditable(true);
            getJTextFieldTipoDocumento().setEditable(true);
        }
        catch (Exception e)
        {
            logger.error("Error al pintar la imagen", e);
        }
    }
	
	private void saveImagen(){
				
		if (indiceImagenSeleccionada > -1 && lstImagenes.size()>indiceImagenSeleccionada && lstImagenes.get(indiceImagenSeleccionada)!= null){
			((ImagenCatastro)lstImagenes.get(indiceImagenSeleccionada)).setNombre(getJTextFieldNombre().getText());
			((ImagenCatastro)lstImagenes.get(indiceImagenSeleccionada)).setExtension(getJTextFieldExtension().getText());
			((ImagenCatastro)lstImagenes.get(indiceImagenSeleccionada)).setTipoDocumento(getJTextFieldTipoDocumento().getText());
		}
    	
	}
	
	public void setImageSelected(ImagenCatastro imageSelected)
    {
        this.imageSelected = imageSelected;
    }
	
	private void load(ImagenCatastro imagen){

		if (imagen != null){
			
			getJTextFieldNombre().setText(imagen.getNombre() != null ? imagen.getNombre(): "");
			getJTextFieldExtension().setText(imagen.getExtension() != null ? imagen.getExtension() : "");
			getJTextFieldTipoDocumento().setText(imagen.getTipoDocumento() != null ? imagen.getTipoDocumento() : "");
		}
		else{
			
			getJTextFieldNombre().setText("");
			getJTextFieldExtension().setText("");
			getJTextFieldTipoDocumento().setText("");
		}

	}
	
	public void actualizarModelo()
    {
        Enumeration e = jPanelImgThumbnails.elements();
        int j=0;
        for (int i = imagenInicialPanel; i <= imagenFinalPanel; i++)
        {
            try
            {
                if(i == lstImagenes.size()) break;
                ImagenCatastro auxImagen = (ImagenCatastro) lstImagenes.get(i);
                PintarImagenCatastro auxPintarImagen = (PintarImagenCatastro) e.nextElement();
                if (imageSelected == null){
                	auxPintarImagen.setSelected(false);
                }
                else{
                	auxPintarImagen.setSelected(auxImagen.equals(imageSelected));
                }
//                auxPintarImagen.setSelected(imageSelected == null ? false : auxImagen.getImagen().equals(imageSelected.getImagen()));
                j++;
                auxPintarImagen.setImagen(auxImagen);
                auxPintarImagen.setIndicePanel(i);
            }
            catch (Exception ex)
            {
                logger.error("Error al actualizar el modelo", ex);
            }
        }
        
        habilitarBotones();
        
        for (int x=j; x<jPanelImgThumbnails.size();x++)
        {
             try{
            	 
                 PintarImagenCatastro aux=((PintarImagenCatastro) e.nextElement());
                 aux.setImagen(null);
                 aux.setSelected(false);
                 
             }
             catch(Exception ex)
             {
                 logger.error("Error al pintar la imagen ", ex);
             }
        }
    }
	
	public void pintarImagen(ImagenCatastro imagen)
	{
		try
		{
			if (imagen != null){
								
				getJPanelPintarImagenCatastro(this).setImagen(imagen);
			}
		}
		catch(Exception e)
		{
			logger.error("Error",e);
		}
	}
	
	public void habilitarBotones()
    {
        
        if(lstImagenes != null && lstImagenes.size()>0 && indiceImagenSeleccionada < lstImagenes.size()-1)
            getJButtonImagenMas().setEnabled(true);
        else
        	getJButtonImagenMas().setEnabled(false);

        if(lstImagenes != null && lstImagenes.size()>0 && indiceImagenSeleccionada > 0)
            getJButtonImagenMenos().setEnabled(true);
        else
            getJButtonImagenMenos().setEnabled(false);
        
        if(lstImagenes != null && lstImagenes.size()>0 && imagenInicialPanel > 0)
            getJButtonThumbnailMenos().setEnabled(true);
        else
        	getJButtonThumbnailMenos().setEnabled(false);

        if(lstImagenes != null && lstImagenes.size()>0 && imagenFinalPanel < lstImagenes.size()-1)
            getJButtonThumbnailMas().setEnabled(true);
        else
            getJButtonThumbnailMas().setEnabled(false);
    }
	
	private ImagenCatastro addImagen(){

		JFileChooser chooser = new JFileChooser();
		com.geopista.app.utilidades.GeoPistaFileFilter filter = new com.geopista.app.utilidades.GeoPistaFileFilter();

		/* filtro para imágenes */
		filter.addExtension("jpg");
		filter.addExtension("gif");
		filter.addExtension("bmp");
		filter.addExtension("png");

		chooser.setFileFilter(filter);

		chooser.setMultiSelectionEnabled(false);
		if(chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION){
			return null;
		}
		
		ImagenCatastro imagen = null;
		
		try{

			File fichero = chooser.getSelectedFile();
			
			String nombre = GUIUtil.nameWithoutExtension(fichero);
			String extension = getExtension(fichero);
			
			imagen = new ImagenCatastro();
			imagen.setNombre(nombre);
			imagen.setExtension(extension);
			imagen.setTipoDocumento(extension);
			imagen.setFoto(getContenido(fichero));
			if (lstImagenes == null){
				lstImagenes = new ArrayList();
			}
			lstImagenes.add(imagen);		
			
			int index=lstImagenes.size();
	        if(imagenFinalPanel<(index-1))
	        {
	           imagenInicialPanel=index-jPanelImgThumbnails.size();
	           imagenFinalPanel= index-1;
	        }

		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,I18N.get("InfCatastralFisicoEconomicoPlugIn","infCatastralFisicoEconomico.img.panel.buttons.add.error"),
					AppContext.getMessage("GeopistaName"),JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return imagen;
		
	}
	
	public static byte[] getContenido(File file) throws Exception{

        InputStream is= new FileInputStream(file);
        long length= file.length();

        if (length > Integer.MAX_VALUE) throw new ACException("El fichero es demasiado grande.");

        byte[] bytes= new byte[(int)length];

        // Leemos en bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Comprobamos haber leido todos los bytes del fichero
        if (offset < bytes.length) throw new IOException("No se ha podido leer completamente el fichero "+file.getName());

        is.close();

        return bytes;
    }
	
	public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
	

	public void seleccionar(ImagenCatastro imagen) {
		// TODO Auto-generated method stub
		
	}

}  
