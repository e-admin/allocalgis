package com.geopista.ui.plugin.infcattitularidad.dialogs;

import java.util.Collection;
import javax.swing.JPanel;
import com.geopista.ui.plugin.infcattitularidad.paneles.InfCatatralTitularidadPanel;


public class DialogoVerInfCatastralTitularidad extends javax.swing.JDialog{

	private JPanel todoPanel;
	private JPanel panel;
	
	

	/**
     * Constructor de la calse. Inicializa el panel y lo muestra. Necesia el parametro parent (Frame) y modal (boolean)
     * para pasarselo al padre.
     *
     * @param parent JFrame
     * @param modal boolean
     * @param convenioExpediente String
     */
    public DialogoVerInfCatastralTitularidad(java.awt.Frame parent, boolean modal, Collection coll, String convenio, String title)
    {
		super(parent, modal);
		this.setTitle(title);
        inicializaDialogo(coll, convenio);
	}
    
    /**
     * Inicializa los elemento de la gui con sus determinados valores y eventos.
     */
    private void inicializaDialogo(Collection coll, String convenio)
    {

    	getContentPane().add(new InfCatatralTitularidadPanel(false, coll, convenio));
    }
    
    
}
