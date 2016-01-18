/**
 * ImportSelectPanelFXCC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.intercambio.importacion.paneles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.geopista.app.AppContext;
import com.geopista.app.catastro.gestorcatastral.MainCatastro;
import com.geopista.app.catastro.intercambio.edicion.utils.ParcelaListCellRenderer;
import com.geopista.app.catastro.intercambio.images.IconLoader;
import com.geopista.app.catastro.intercambio.importacion.utils.ImportarUtils;
import com.geopista.app.catastro.intercambio.importacion.utils.UtilImportacion;
import com.geopista.app.catastro.model.beans.ImagenCatastro;
import com.geopista.server.administradorCartografia.ACException;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.ui.GUIUtil;


public class ImportSelectPanelFXCC extends JPanel 
{
	private AppContext application = (AppContext) AppContext.getApplicationContext();
	private Blackboard blackboard = application.getBlackboard();
    private JPanel jPanelLateral = null;
    private JPanel jPanelCentral = null;
    private JPanel jPanelAsociacionInfo = null;
    private JButton jButtonAsociarContinuar = null;
    private JLabel jLabelImagen = null;
    private JLabel jLabelParcela = null;
    private JComboBox jComboBoxParcela = null;
    private JButton jButtonFichDXF = null;
    private JLabel jLabelFichFXCC = null;
    private JTextField jTextFieldFichDXF = null;
    
    private static final String OPEN_FILE = "abrir.gif";
    private JLabel jLabelASC = null;
    private JTextField jTextFieldFichASC = null;
    private JButton jButtonFichASC = null;
    
    
    private JPanel jPanelAsociarImagenes = null;
    private TablaAsociarImagenesParcelasImportacionFXCC impotacionTablaImagenesInmuebles ;
	private JPanel jPanelBotonearAsociarImagenes = null;
    private JButton jButtonAnadirImagen = null;
    private JButton jButtonEliminarImagen = null;
    private ArrayList lstImagenes = null;

	/**
     * This method initializes 
     */
    public ImportSelectPanelFXCC(String title) {
        super();
        //initialize(title, ModuloCatastralFrame.DIM_X/2,
        //        ModuloCatastralFrame.DIM_Y /2);
        initialize(title);
    }
    
    /**
     * This method initializes this
     * 
     */    
    private void initialize(String title) {
        
        //this.setPreferredSize(new Dimension(dimx, dimy));
        this.setLayout(new GridBagLayout());
        this.add(getJPanelLateral(), 
                new GridBagConstraints(0, 0, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER,
                        new Insets(0, 5, 0, 5), 0, 0));
        
        this.add(getJPanelCentral(), 
                new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 5, 0, 5), 0, 0));
        
        jPanelAsociacionInfo.setBorder(
                BorderFactory.createTitledBorder(title));        
    }
    
    /**
     * This method initializes jPanelLateral	
     * 	
     * @return javax.swing.JPanel	
     */
    public JPanel getJPanelLateral()
    {
        if (jPanelLateral == null)
        {
            jLabelImagen = new JLabel();
            jLabelImagen.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            jLabelImagen.setIcon(IconLoader.icon(MainCatastro.BIG_PICTURE_LOCATION));
            jPanelLateral = new JPanel(new GridBagLayout());
            
            jPanelLateral.add(jLabelImagen, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                   new Insets(0, 0, 0, 0), 0, 0));
        }
        return jPanelLateral;
    }
    
    public JLabel getLabelImagen()
    {
        return jLabelImagen;
    }
    
    /**
     * This method initializes jPanelCentral	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanelCentral()
    {
        if (jPanelCentral == null)
        { 
            jPanelCentral = new JPanel(new GridBagLayout()); 
            jPanelCentral.add(getJPanelIntercambioInfo(), 
                    new GridBagConstraints(0, 0, 1, 1, 1, 1,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 5, 0, 5), 0, 0));   
            
           
        }
        return jPanelCentral;
    }
    
   
    /**
     * This method initializes jButtonFichDXF	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonAsociarContinuar()
    {
        if (jButtonAsociarContinuar == null)
        {
        	jButtonAsociarContinuar = new JButton();
        	jButtonAsociarContinuar.setPreferredSize(new Dimension(150,25));
            jButtonAsociarContinuar.setText(I18N.get("Importacion","importar.infografica.boton.asociarcontinuar"));
            jButtonAsociarContinuar.setEnabled(false);
           
	    }
        return jButtonAsociarContinuar;
    }
    
    
    /**
     * This method initializes jPanelIntercambioInfo  
     *  
     * @return javax.swing.JPanel   
     */
    private JPanel getJPanelIntercambioInfo()
    {
        if (jPanelAsociacionInfo == null)
        {               
            jPanelAsociacionInfo = new JPanel(new GridBagLayout());            
            jLabelASC = new JLabel(I18N.get("Importacion","importar.infografica.ficheroasc"), JLabel.LEFT);
            jLabelFichFXCC = new JLabel(I18N.get("Importacion","importar.infografica.ficherodxf"), JLabel.LEFT);            
            jLabelParcela = new JLabel(I18N.get("Importacion","importar.infografica.parcela"), JLabel.LEFT);

            jPanelAsociacionInfo.add(jLabelParcela, 
                    new GridBagConstraints(0, 0, 2, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 100, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getJComboBoxParcela(), 
                    new GridBagConstraints(0, 1, 1, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 100, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(jLabelFichFXCC, 
                    new GridBagConstraints(0, 2, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 100, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getJTextFieldFichDXF(), 
                    new GridBagConstraints(0, 3, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 100, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getJButtonFichDXF(), 
                    new GridBagConstraints(1, 3, 1, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(jLabelASC, 
                    new GridBagConstraints(0, 4, 2, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(15, 100, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getJTextFieldFichASC(), 
                    new GridBagConstraints(0, 5, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 100, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getJButtonFichASC(), 
                    new GridBagConstraints(1, 5, 1, 1, 1, 0, 
                            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 11, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getjPanelAsociarImagenes(), 
                    new GridBagConstraints(0, 6, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(30, 90, 0, 0), 0, 0));
            jPanelAsociacionInfo.add(getJButtonAsociarContinuar(), 
                    new GridBagConstraints(0, 7, 1, 1, 1, 0, 
                            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(40, 10, 0, 0), 0, 0));
            
        }
        return jPanelAsociacionInfo;
    }
    /**
     * This method initializes jTextFieldCertDigital	
     * 	
     * @return javax.swing.JTextField	
     */
    public JComboBox getJComboBoxParcela()
    {
        if (jComboBoxParcela == null)
        {
            jComboBoxParcela = new JComboBox();  
            jComboBoxParcela.setPreferredSize(new Dimension(50,25));
            jComboBoxParcela.setRenderer(new ParcelaListCellRenderer());
        }
        return jComboBoxParcela;
    }
    /**
     * This method initializes jButtonFichDXF	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonFichDXF()
    {
        if (jButtonFichDXF == null)
        {
            jButtonFichDXF = new JButton();
            jButtonFichDXF.setIcon(IconLoader.icon(OPEN_FILE));
        }
        return jButtonFichDXF;
    }
     
    /**
     * This method initializes jTextFieldFichFXCC	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getJTextFieldFichDXF()
    {
        if (jTextFieldFichDXF == null)
        {
            jTextFieldFichDXF = new JTextField();
            jTextFieldFichDXF.setEditable(false);
            jTextFieldFichDXF.setBackground(new Color(200, 200, 200));
        }
        return jTextFieldFichDXF;
    }
    /**
     * This method initializes jTextFieldFichASC	
     * 	
     * @return javax.swing.JTextField	
     */
    public JTextField getJTextFieldFichASC()
    {
        if (jTextFieldFichASC == null)
        {
            jTextFieldFichASC = new JTextField();
            jTextFieldFichASC.setEditable(false);
            jTextFieldFichASC.setBackground(new Color(200, 200, 200));
        }
        return jTextFieldFichASC;
    }
    /**
     * This method initializes jButtonFichASC	
     * 	
     * @return javax.swing.JButton	
     */
    public JButton getJButtonFichASC()
    {
        if (jButtonFichASC == null)
        {
            jButtonFichASC = new JButton();
            jButtonFichASC.setIcon(IconLoader.icon(OPEN_FILE));
        }
        return jButtonFichASC;
    }    
    
    
    
    public JPanel getjPanelAsociarImagenes() {
    	
    	if (jPanelAsociarImagenes == null)
        {
    		UtilImportacion.inicializarIconos();
    		jPanelAsociarImagenes = new JPanel(new GridBagLayout());
    		jPanelAsociarImagenes.setBorder(new TitledBorder(I18N.get("Importacion","importar.infografica.seleccion.imagenes")));
    		
    		impotacionTablaImagenesInmuebles = new TablaAsociarImagenesParcelasImportacionFXCC("importar.infografica.seleccion.imagen.lista.imagenes");
    		impotacionTablaImagenesInmuebles.setPreferredSize(new Dimension(390, 200));
    		
    		jPanelAsociarImagenes.add(impotacionTablaImagenesInmuebles, 
    				new GridBagConstraints(0, 1, 1, 1, 1, 1,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 20, 0, 0), 0, 0));
    		
    		jPanelAsociarImagenes.add(getjPanelBotonearAsociarImagenes(), 
    				new GridBagConstraints(1, 1, 1, 1, 1, 1,
                            GridBagConstraints.WEST, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
    		
    		//impotacionTablaImagenesInmuebles.cargaDatosTabla(new ArrayList());
    		limpiarTabla();
    		

        }
		return jPanelAsociarImagenes;
	}
 
	public JPanel getjPanelBotonearAsociarImagenes() {
		if (jPanelBotonearAsociarImagenes == null){
			jPanelBotonearAsociarImagenes = new JPanel();
			jPanelBotonearAsociarImagenes.setPreferredSize(new Dimension(30, 100));
			jButtonAnadirImagen = new JButton();
			jButtonAnadirImagen.setToolTipText(I18N.get("Importacion",
            	"importar.infografica.seleccion.imagen.anadir.imagen.hint"));

			jButtonAnadirImagen.setIcon(UtilImportacion.iconoAnadir);
			jButtonAnadirImagen.setPreferredSize(new Dimension(20, 20));
			
			jButtonAnadirImagen.addActionListener(new ActionListener()
        	{
        	public void actionPerformed(ActionEvent e)
            {
                try
                {
                	addImagen();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
                }); 
			
			
			jButtonEliminarImagen = new JButton();
			jButtonEliminarImagen.setToolTipText(I18N.get("Importacion",
				"importar.infografica.seleccion.imagen.eliminar.imagen.hint"));
			jButtonEliminarImagen.setIcon(UtilImportacion.iconoDelete);
			jButtonEliminarImagen.setPreferredSize(new Dimension(20, 20));
			
			jButtonEliminarImagen.addActionListener(new ActionListener()
        	{
        	public void actionPerformed(ActionEvent e)
            {
                try
                {
                	deleteImagen();
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
                }); 
		    
			jPanelBotonearAsociarImagenes.add(jButtonAnadirImagen, 
	        		 new GridBagConstraints(0, 0, 0, 0, 0, 0,
	                         GridBagConstraints.NORTH, GridBagConstraints.NONE,
	                         new Insets(0, 0, 0, 0), 0, 0));

			jPanelBotonearAsociarImagenes.add(jButtonEliminarImagen, 
	        		 new GridBagConstraints(0, 1, 0, 0, 0, 0,
	                         GridBagConstraints.NORTH, GridBagConstraints.NONE,
	                         new Insets(0, 0, 0, 0), 0, 0));
			
			
		}
		return jPanelBotonearAsociarImagenes;
	}
	
	
	public JButton getjButtonAnadirImagen() {
		return jButtonAnadirImagen;
	}

	public JButton getjButtonEliminarImagen() {
		return jButtonEliminarImagen;
	}
	
	private void deleteImagen(){
		
		int imgSeleccionada = impotacionTablaImagenesInmuebles.getImagenSeleccionada();
		if(imgSeleccionada>=0){
			lstImagenes.remove(impotacionTablaImagenesInmuebles.getImagenSeleccionada());
		}
        else{
        	JOptionPane.showMessageDialog(this, I18N.get("Importacion",
        		"importar.infografica.seleccion.imagen.msgEliminar"));
        }
		impotacionTablaImagenesInmuebles.cargaDatosTabla(lstImagenes);
		
	}

	private void addImagen(){

		JFileChooser chooser = new JFileChooser();
		com.geopista.app.utilidades.GeoPistaFileFilter filter = new com.geopista.app.utilidades.GeoPistaFileFilter();

		/* filtro para imágenes */
		filter.addExtension("jpg");
		filter.addExtension("gif");
		filter.addExtension("bmp");
		filter.addExtension("png");

		chooser.setFileFilter(filter);

		File currentDirectory = (File) blackboard.get(ImportarUtils.LAST_IMPORT_DIRECTORY_IMAGENES);        
        chooser.setCurrentDirectory(currentDirectory);  
       
        
		chooser.setMultiSelectionEnabled(false);
		int returnVal = chooser.showOpenDialog(this);       
		ImagenCatastro imagen = null;
		 blackboard.put(ImportarUtils.LAST_IMPORT_DIRECTORY_IMAGENES, chooser.getCurrentDirectory());
		if (returnVal == JFileChooser.APPROVE_OPTION)
        {
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

				impotacionTablaImagenesInmuebles.cargaDatosTabla(lstImagenes);

			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null,I18N.get("Importacion","importar.infografica.seleccion.imagen.add.imagen.error"),
						null,JOptionPane.ERROR_MESSAGE);
				//return null;
			}					
        }
		//return imagen;
		
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
	
	public ArrayList getLstImagenes() {
		return lstImagenes;
	}

	public void setLstImagenes(ArrayList lstImagenes) {
		this.lstImagenes = lstImagenes;
	}
	
    public TablaAsociarImagenesParcelasImportacionFXCC getImpotacionTablaImagenesInmuebles() {
		return impotacionTablaImagenesInmuebles;
	}

	public void setImpotacionTablaImagenesInmuebles(
			TablaAsociarImagenesParcelasImportacionFXCC impotacionTablaImagenesInmuebles) {
		this.impotacionTablaImagenesInmuebles = impotacionTablaImagenesInmuebles;
	}
	
	public void limpiarTabla(){
		
		this.impotacionTablaImagenesInmuebles.cargaDatosTabla(new ArrayList());
		lstImagenes = new ArrayList();
	}
}  
