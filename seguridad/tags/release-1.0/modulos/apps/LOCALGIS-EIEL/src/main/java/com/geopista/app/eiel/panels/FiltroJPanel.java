package com.geopista.app.eiel.panels;

import java.awt.BorderLayout;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.LocalGISEIELClient;
import com.geopista.app.eiel.beans.filter.LCGNodoEIEL;
import com.geopista.app.eiel.utils.NodoComparatorByTraduccion;
import com.geopista.protocol.administrador.Acl;
import com.geopista.server.administradorCartografia.AdministradorCartografiaClient;
import com.vividsolutions.jump.I18N;


public class FiltroJPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame desktop;
	private AppContext aplicacion;
	private String locale;
	private String tipo;
	
	
	//Lista de filtros por cada epigrafe que se cargue.
	private ArrayList<FiltroBox> listaPestañasFiltros;
	
	private JTabbedPane panelPestañasNodos;
	
	//public static final String CHANGE_TAB="CHANGE_TAB";
    
    private ArrayList <ActionListener> actionListeners= new ArrayList<ActionListener>();
    private LocalGISEIELClient eielClient;
	
    private HashMap<String,String> listaFiltros;
    
    private ArrayList listaPatrones=new ArrayList();
    
    //FiltroBox filtro;

	//private static final Log logger = LogFactory.getLog(FiltroJPanel.class);
    private static  org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FiltroJPanel.class);

	
	public FiltroJPanel(JFrame desktop, String tipo,HashMap<String,String> listaFiltros, String locale) {
 	   aplicacion= (AppContext) AppContext.getApplicationContext();
        this.desktop= desktop;
        this.locale= locale;
        this.tipo= tipo;
        this.listaFiltros=listaFiltros;
        
	    eielClient = new LocalGISEIELClient(aplicacion.getString(AppContext.GEOPISTA_CONEXION_ADMINISTRADORCARTOGRAFIA)+"/EIELServlet");
	    listaPestañasFiltros=new ArrayList<FiltroBox>();
        
        initComponents();
 }

	private void initComponents() {
		panelPestañasNodos = new JTabbedPane();		
		
	
		
		Collection<Object> nodos=getNodosEIEL(tipo,locale);
		
		
		
		
		logger.debug("Numero de nodos:"+nodos.size());
		Iterator it=nodos.iterator();
		while (it.hasNext()){
			LCGNodoEIEL nodoEIEL=(LCGNodoEIEL)it.next();
			//Las depuradoras las gestionamos de forma distinta.
			//Si el numero de elementos es >1 estamos listando un elemento raiz en el que para
			//el caso de las depuradoras no nos interesa diferenciar D1 y D2
			if (nodos.size()>1){
				if ((nodoEIEL.getTagTraduccion()!=null) && (nodoEIEL.getTagTraduccion().equals("eiel.D1") || 
						nodoEIEL.getTagTraduccion().equals("eiel.D2")))
					continue;
			}
			
			logger.debug("Nodo:"+nodoEIEL.getTraduccion());			
			JScrollPane filtroJScrollPane = new javax.swing.JScrollPane();
			panelPestañasNodos.addTab(nodoEIEL.getTraduccion(),filtroJScrollPane);
			
			String filtrosSeleccionados=listaFiltros.get(nodoEIEL.getClave());
			FiltroBox filtro = new FiltroBox(nodoEIEL.getNodo(),nodoEIEL.getNombreFiltro(),filtrosSeleccionados,locale);
			filtroJScrollPane.setViewportView(filtro);
			listaPestañasFiltros.add(filtro);
			listaPatrones.add(nodoEIEL);
		}

		
		add(panelPestañasNodos, BorderLayout.CENTER); 
	}
	
	public void habilitarFiltros(boolean activar){
		for (int i=0;i<listaPestañasFiltros.size();i++){
			FiltroBox filtro=listaPestañasFiltros.get(i);
			filtro.habilitarFiltros(activar);
		}
	}
		
	public ArrayList getListaPatrones(){
		return listaPatrones;
	}
	
	/**
	 * Lista de Filtros por cada panel
	 * @param nucleoSeleccionado 
	 * @return
	 */
	public HashMap<String,String> getFiltros(String codEntidad,String codNucleo){
		HashMap<String,String> filtros=new HashMap<String,String>();
		
		for (int i=0;i<listaPestañasFiltros.size();i++){
			FiltroBox filtro=listaPestañasFiltros.get(i);
			String nombrefiltro=filtro.getNombre();
			String valorfiltro=filtro.getValue(codEntidad,codNucleo);
			if (filtro.isFiltroSelecionado()){					
				//Por ejemplo filtro_captaciones="Sentencia SQL"
				if (nombrefiltro!=null)
					filtros.put(nombrefiltro,valorfiltro);
			}
		}
		return filtros;
		
	}
	
	public String getTipo(){
		return tipo;
	}

	private Collection<Object> getNodosEIEL(String nodo,String locale){
		Collection<Object> c=null;
        try {
			c = eielClient.getNodosEIEL(nodo,locale);
		} catch (Exception e) {
			e.printStackTrace();
		}
    		
		//Los ordenamos alfabeticamente		
		Object[] arrayNodos= c.toArray();
    	Arrays.sort(arrayNodos,new NodoComparatorByTraduccion());

    	//Los devolvemos ordenados
    	c.clear();
		for (int i = 0; i < arrayNodos.length; ++i) {
			LCGNodoEIEL nodoEIEL = (LCGNodoEIEL) arrayNodos[i];
			c.add(nodoEIEL);
		}

        return c;
       
    }
	
	
}
