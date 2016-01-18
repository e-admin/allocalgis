/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dialogs;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;
import org.uva.geotools.graph.structure.line.XYNode;

import com.geopista.app.AppContext;
import com.geopista.app.reports.GenerarInformeExterno;
import com.geopista.app.utilidades.CUtilidadesComponentes;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.WriteRoutePathInfoWithDialog;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.InfoRouteStretchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.beans.TurnRouteStreetchBean;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.renderers.InfoRouteListRenderer;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.util.StringUtil;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.ErrorDialog;
import com.vividsolutions.jump.workbench.ui.zoom.ZoomToSelectedItemsPlugIn;

/**
 * @author javieraragon
 *
 */
public class InfoRouteDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8525427530861575165L;


	private JList infoRouteJList = null;
	private Collection<InfoRouteStretchBean> infoRoutesStretchBeansCollection = null;
	private PlugInContext pluginContext = null;

	//Panels
	private JPanel rootPanel = null;
	private JPanel infoListPanel = null;
	private JPanel botoneraPanel = null;
	// Buttons
	private JButton closeDialogoButton = null;
	private JButton printInfoRouteButton = null;
	private JButton zoomToRouteButton = null;
	//	private JButton zoomToRouteStretch = null;

	private ZoomToSelectedItemsPlugIn zoomToSelectedItemsPlugIn = new ZoomToSelectedItemsPlugIn();

	// Elementos para imprimir informe
	private Logger logger = Logger.getLogger(InfoRouteDialog.class);
	private String reportFile = "";
	private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();

	public InfoRouteDialog(PlugInContext context, String title, ArrayList<InfoRouteStretchBean> list){
		super(context.getWorkbenchGuiComponent().getMainFrame(),title,false);
		WriteRoutePathInfoWithDialog.inicializarIdiomaAvisosPanels();
		this.infoRoutesStretchBeansCollection = list;
		this.initialize();
		this.pluginContext = context;

		this.pack();
		this.setSize(this.getWidth() + 20, this.getHeight());
		this.setResizable(false);
		this.setVisible(true);
	}




	private void initialize() {
		this.setLayout(new GridBagLayout());


		this.add(this.getRootPanel(), 
				new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.BOTH, 
						new Insets(0, 5, 0, 5), 0, 0));

		this.add(this.getBotoneraPanel(), 
				new GridBagConstraints(0, 1, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
						GridBagConstraints.HORIZONTAL, 
						new Insets(0, 5, 0, 5), 0, 10));


	}



	private JPanel getRootPanel(){
		if (rootPanel == null){
			rootPanel = new JPanel(new GridBagLayout());

			rootPanel.add(this.getInfoListPanel(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 5));


		}
		return rootPanel;
	}

	private JPanel getInfoListPanel(){
		if (infoListPanel == null){
			infoListPanel = new JPanel(new GridBagLayout());

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(getInfoRouteJList());
			scrollPane.setAutoscrolls(true);
			//			scrollPane.setSize(10,10);

			infoListPanel.add(scrollPane, 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.NORTH, 
							GridBagConstraints.BOTH, 
							new Insets(0, 5, 0, 5), 0, 5));


		}
		return infoListPanel;
	}

	private JPanel getBotoneraPanel(){
		if (botoneraPanel == null){
			botoneraPanel = new JPanel(new GridBagLayout());


			botoneraPanel.add(this.getZoomToRouteButton(), 
					new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));

			/*botoneraPanel.add(this.getPrintInfoRouteButton(), 
					new GridBagConstraints(1, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));*/

			botoneraPanel.add(this.getCloseDialogoButton(), 
					new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.CENTER, 
							GridBagConstraints.NONE, 
							new Insets(0, 5, 0, 5), 0, 5));
		}
		return botoneraPanel;
	}

	private JList getInfoRouteJList(){
		if (infoRouteJList == null){

			DefaultListModel defaultListModel = new DefaultListModel();
			if (this.infoRoutesStretchBeansCollection!= null && !this.infoRoutesStretchBeansCollection.isEmpty()){
				Iterator<InfoRouteStretchBean> it = this.infoRoutesStretchBeansCollection.iterator();
				while(it.hasNext()){
					defaultListModel.addElement(it.next());
				}

			}

			infoRouteJList = new JList(defaultListModel);			
			infoRouteJList.setCellRenderer(new InfoRouteListRenderer());
			infoRouteJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


			infoRouteJList.addMouseMotionListener(new MouseMotionListener(){
				@Override public void mouseDragged(MouseEvent e) { }

				@Override public void mouseMoved(MouseEvent e) {
					// Like Mouse Over
					onInfoRouteListMosueOver(e.getPoint());
				}				
			});

			infoRouteJList.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					// On Mouse Click
					onInfoRouteListeMouseClicked();
				}

				@Override public void mouseEntered(MouseEvent e) { }

				@Override public void mouseExited(MouseEvent e) { }

				@Override public void mousePressed(MouseEvent e) { }

				@Override public void mouseReleased(MouseEvent e) { }

			});

			infoRouteJList.addKeyListener(new KeyListener() {

				@Override public void keyTyped(KeyEvent e) { }

				@Override public void keyReleased(KeyEvent e) { }

				@Override public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
						onInfoRouteListEnterKeyPressedDo();
					}
				}
			});
		}
		return infoRouteJList;
	}


	private void onInfoRouteListEnterKeyPressedDo() {
		Object value = null;
		try{

			value = infoRouteJList.getSelectedValue();

			ArrayList<Geometry> geoArrayList = new ArrayList<Geometry>();

			if (value != null && value instanceof InfoRouteStretchBean){
				InfoRouteStretchBean actualStretchBean = (InfoRouteStretchBean) value;

				if (actualStretchBean!=null && actualStretchBean.getGeometries()!=null && !actualStretchBean.getGeometries().isEmpty()){
					geoArrayList.addAll(actualStretchBean.getGeometries());
				}

				if (value instanceof TurnRouteStreetchBean){
					GeometryFactory geoFactory = new GeometryFactory();
					if (((TurnRouteStreetchBean)value).getTurnNode()!=null){
						geoArrayList.add(geoFactory.createPoint(((XYNode)((TurnRouteStreetchBean)value).getTurnNode()).getCoordinate()));
					}
				}

				zoomToSelectedItemsPlugIn.zoom(
						geoArrayList,
						pluginContext.getLayerViewPanel());
			}


		}catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.show(null, 
					"Error resaltar un tramo de la ruta.", 
					I18N.get("routedescription","routeengine.route.description.error.route.not.zoom.message"), 
					StringUtil.stackTrace(ex));
		}
	}


	private void onInfoRouteListeMouseClicked() {
		Object value = null;
		try{

			value = infoRouteJList.getSelectedValue();

			ArrayList<Geometry> geoArrayList = new ArrayList<Geometry>();

			if (value != null && value instanceof InfoRouteStretchBean){
				InfoRouteStretchBean actualStretchBean = (InfoRouteStretchBean) value;

				if (actualStretchBean!=null && actualStretchBean.getGeometries()!=null && !actualStretchBean.getGeometries().isEmpty()){
					geoArrayList.addAll(actualStretchBean.getGeometries());
				}

				if (value instanceof TurnRouteStreetchBean){
					GeometryFactory geoFactory = new GeometryFactory();
					if (((TurnRouteStreetchBean)value).getTurnNode()!=null){
						geoArrayList.add(geoFactory.createPoint(((XYNode)((TurnRouteStreetchBean)value).getTurnNode()).getCoordinate()));
					}
				}

				zoomToSelectedItemsPlugIn.zoom(
						geoArrayList,
						pluginContext.getLayerViewPanel());
			}


		}catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.show(null, 
					"Error resaltar un tramo de la ruta.", 
					I18N.get("routedescription","routeengine.route.description.error.route.not.zoom.message"), 
					StringUtil.stackTrace(ex));
		}
	}


	private void onInfoRouteListMosueOver(Point point) {
		Object value = null;
		try{
			int pos = infoRouteJList.locationToIndex(point);
			value = ((DefaultListModel)infoRouteJList.getModel()).get(pos);

			if (value != null && value instanceof InfoRouteStretchBean){
				InfoRouteStretchBean actualStretchBean = (InfoRouteStretchBean) value;
				ArrayList<Geometry> geoArrayList = new ArrayList<Geometry>();
				if (actualStretchBean!=null && actualStretchBean.getGeometries()!=null && !actualStretchBean.getGeometries().isEmpty()){
					geoArrayList.addAll(actualStretchBean.getGeometries());
				}	

				if (value instanceof TurnRouteStreetchBean){
					GeometryFactory geoFactory = new GeometryFactory();
					if (((TurnRouteStreetchBean)value).getTurnNode()!=null){
						geoArrayList.add(geoFactory.createPoint(((XYNode)((TurnRouteStreetchBean)value).getTurnNode()).getCoordinate()));
					}
				}

				GeometryFactory geoFactory = new GeometryFactory();
				GeometryCollection geoCollection = new GeometryCollection(geoArrayList.toArray(new Geometry[geoArrayList.size()]), geoFactory);

				pluginContext.getLayerViewPanel().flash(geoCollection);
				
				try{
//					Component comp = getInfoListPanel().getComponentAt(point);
//					 if (comp != null){
//						comp.setBackground(Color.LIGHT_GRAY);
//						 comp.setForeground(Color.RED);
//					 }
				}catch (Exception e) {
					e.printStackTrace();
				}
			}


		}catch (Exception ex) {
			ex.printStackTrace();
			ErrorDialog.show(null, 
					"Error resaltar un tramo de la ruta.", 
					I18N.get("routedescription","routeengine.route.description.error.route.not.zoom.message"), 
					StringUtil.stackTrace(ex));
		}

	}



	private JButton getCloseDialogoButton(){
		if (closeDialogoButton == null){
			closeDialogoButton = new JButton(I18N.get("routedescription","routeengine.route.description.dialog.button.close.label"));
			closeDialogoButton.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onCerrarDialogoButtonDo();
				}			
			});
		}
		return closeDialogoButton;
	}

	private JButton getPrintInfoRouteButton(){
		if (printInfoRouteButton == null){
			printInfoRouteButton = new JButton(I18N.get("routedescription","routeengine.route.description.dialog.button.print.route.label"));
			printInfoRouteButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					onPrintInfoRouteButtonDo();
				}		
			});
		}
		return printInfoRouteButton;
	}

	private JButton getZoomToRouteButton(){
		if (zoomToRouteButton == null){
			zoomToRouteButton = new JButton(I18N.get("routedescription","routeengine.route.description.dialog.button.zoom.route.label"));
			zoomToRouteButton.addActionListener(new ActionListener(){
				@Override public void actionPerformed(ActionEvent e) {
					onZoomToRouteButtonDo();
				}		
			});
		}
		return zoomToRouteButton;
	}


	private void onCerrarDialogoButtonDo() {
		this.setVisible(false);
		this.dispose();
		try {
			finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}	

	private void onPrintInfoRouteButtonDo() {
		
		onZoomToRouteButtonDo();
		
		generarInformeImprimibleRuta(this.infoRoutesStretchBeansCollection);
	}	

	private void onZoomToRouteButtonDo() {
		if (this.pluginContext != null){

			//			GeometryFactory geoFactory = new GeometryFactory();
			ArrayList<Geometry> geoArrayList = new ArrayList<Geometry>();
			if (this.infoRoutesStretchBeansCollection!=null && !this.infoRoutesStretchBeansCollection.isEmpty()){
				Iterator<InfoRouteStretchBean> it = this.infoRoutesStretchBeansCollection.iterator();
				while(it.hasNext()){
					InfoRouteStretchBean actualStretchBean = it.next();
					if (actualStretchBean!=null && actualStretchBean.getGeometries()!=null && !actualStretchBean.getGeometries().isEmpty()){
						geoArrayList.addAll(actualStretchBean.getGeometries());
					}
				}
			}
			//			GeometryCollection geoCollection = new GeometryCollection(geoArrayList.toArray(new Geometry[geoArrayList.size()]), geoFactory);

			try {
				zoomToSelectedItemsPlugIn.zoom(geoArrayList, 
						pluginContext.getLayerViewPanel());
			} catch (NoninvertibleTransformException e) {
				e.printStackTrace();
				ErrorDialog.show(null, 
						"Error resaltar la ruta", 
						I18N.get("routedescription","routeengine.route.description.error.route.not.zoom.message"), 
						StringUtil.stackTrace(e));
			}
		}
	}	


	private Map obtenerParametros(Image imagenMapa, String textoRuta){

		Map parametros = new HashMap();
		parametros.put("IMAGE_MAPA", imagenMapa);
		parametros.put("ROUTE_TEXT", textoRuta);
		return parametros;

	}

	public void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(aplicacion.getMainFrame(), mensaje);
	}


	private void generarInformeImprimibleRuta(Collection<InfoRouteStretchBean> infoRoutesStretchBeansCollection2){
		try{
			//			if ((_vExpedientes != null) && (_vExpedientes.size() > 0) && (_vSolicitudes != null) && (_vSolicitudes.size() > 0)){
			if ((infoRoutesStretchBeansCollection2 != null) && !infoRoutesStretchBeansCollection2.isEmpty() ){

				
				// Generamos el informe con la informacion
				this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

				// bajamos del servidor la plantilla seleccionada por el usuario
				boolean encontrado = bajarInformeFiles("informe_ruta.jrxml");

				if (encontrado){          
					try {
						Map parametros = obtenerParametros(this.pluginContext.getLayerViewPanel().createImage(200, 200),"hola \n /n hola \n /n");
						// Le pasamos el nombre del informe (ruta absoluta): 
						GenerarInformeExterno giep=new GenerarInformeExterno(reportFile, parametros);

					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				mostrarMensaje("Para generar el informe es necesario que la búsqueda tenga datos de intervencion.");
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

	}


	private boolean bajarInformeFiles(String name){
		String path= "";
		String url= "";
		String pathDestino= "";
		String plantillasURL= getUrlServer();

		try {

			String localPath = aplicacion.getUserPreference(AppContext.PREFERENCES_DATA_PATH_KEY,AppContext.DEFAULT_DATA_PATH, true);

			path= "plantillas"+ File.separator+ "routes" + File.separator;
			pathDestino = localPath + aplicacion.REPORT_DIR_NAME + File.separator + path;
			plantillasURL +=  "/plantillas/routes/";



			// Comprobamos que el path de las plantillas exista 
			if (!new File(pathDestino).exists()) {
				new File(pathDestino).mkdirs();
			}

			// bajamos la plantilla            
			url = plantillasURL + name;
			pathDestino = pathDestino + name;
			reportFile = pathDestino;
			// Devolvemos verdadero si todo ha sido correcto:
			return CUtilidadesComponentes.GetURLFile(url, pathDestino, "", 0);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return false;
		}

	}




	private String getUrlServer() {
		//String sConn = ;
		return aplicacion.getString("geopista.conexion.serverADMCAR");
	}

}
