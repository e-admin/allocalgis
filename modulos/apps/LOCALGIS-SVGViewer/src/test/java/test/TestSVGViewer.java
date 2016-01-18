/**
 * TestSVGViewer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.tinyline.svg.SVGNode;

import es.satec.svgviewer.SVGViewer;
import es.satec.svgviewer.event.SVGViewerErrorEvent;
import es.satec.svgviewer.event.SVGViewerErrorListener;
import es.satec.svgviewer.event.SVGViewerLinkListener;

public class TestSVGViewer {

	private Display display = null;
	private Shell sShell = null;

	private ToolBar toolBar = null;
	private ToolItem fileItem = null;
	private ToolItem saveItem = null;
	private ToolItem zoomInItem = null;
	private ToolItem zoomOutItem = null;
	private ToolItem panItem = null;
	private ToolItem imageItem = null;
	private ToolItem selItem = null;
	private ToolBar toolBar2 = null;
	private ToolItem polylineItem = null;
	private ToolItem polygonItem = null;
	private ToolItem pointItem = null;
	private ToolItem cancelDrawItem = null;
	private ToolItem layersItem = null;
	
	private SVGViewer viewer;

	private int mode;
	public static final int MODE_NONE = 0;
	public static final int MODE_ZOOMIN = 1;
	public static final int MODE_PAN = 2;
	public static final int MODE_LINK = 3;
	public static final int MODE_POLYLINE = 4;
	public static final int MODE_POLYGON = 5;
	public static final int MODE_POINT = 6;
	
	private SVGLocalGISController controller;
	
	public TestSVGViewer() {
		mode = MODE_NONE;
		display = new Display();
		createSShell();
	}
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.CLOSE);
		sShell.setText("SVG Viewer");
		sShell.setSize(new Point(240, 320));
		//sShell.setMaximized(true);
		sShell.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
		Menu menuBar1 = new Menu(sShell, SWT.BAR);
		sShell.setMenuBar(menuBar1);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.horizontalSpacing = 0;
		sShell.setLayout(gridLayout);
		
		toolBar = new ToolBar(sShell, SWT.WRAP);

		fileItem = new ToolItem(toolBar, SWT.PUSH);
		fileItem.setText("Svg");
		fileItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				loadSVG();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		saveItem = new ToolItem(toolBar, SWT.PUSH);
		saveItem.setText("Save");
		saveItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				saveSVG();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		zoomInItem = new ToolItem(toolBar, SWT.RADIO);
		zoomInItem.setText("Z+");
		zoomInItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_ZOOMIN);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		zoomOutItem = new ToolItem(toolBar, SWT.PUSH);
		zoomOutItem.setText("Z-");
		zoomOutItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				viewer.zoomOut();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		
		panItem = new ToolItem(toolBar, SWT.RADIO);
		panItem.setText("Pan");
		panItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_PAN);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		imageItem = new ToolItem(toolBar, SWT.PUSH);
		imageItem.setText("Im");
		imageItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				loadImage();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		selItem = new ToolItem(toolBar, SWT.RADIO);
		selItem.setText("Sel");
		selItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_LINK);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		toolBar2 = new ToolBar(sShell, SWT.WRAP);

		polylineItem = new ToolItem(toolBar2, SWT.CHECK);
		polylineItem.setText("Lin");
		polylineItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_POLYLINE);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		polygonItem = new ToolItem(toolBar2, SWT.CHECK);
		polygonItem.setText("Pol");
		polygonItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_POLYGON);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		pointItem = new ToolItem(toolBar2, SWT.RADIO);
		pointItem.setText("Pun");
		pointItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_POINT);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		cancelDrawItem = new ToolItem(toolBar2, SWT.PUSH);
		cancelDrawItem.setText("Borra");
		cancelDrawItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				viewer.cancelDraw();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

//		layersItem = new ToolItem(toolBar2, SWT.PUSH);
//		layersItem.setText("Capas");
//		layersItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				ordenCapas();
//			}
//			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
//			}
//		});

		// Crea el visor SVG
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		viewer = new SVGViewer(sShell, SWT.NONE);
		viewer.setLayoutData(gridData);
		viewer.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

		// Prueba de menu contextual
//		viewer.addMenuDetectListener(new MenuDetectListener() {
//			public void menuDetected(MenuDetectEvent e) {
//				System.out.println("Detectado evento de menu");
//				Menu menu = new Menu(sShell, SWT.POP_UP);
//				MenuItem item1 = new MenuItem(menu, SWT.PUSH);
//				item1.setText("Opcion 1");
//				MenuItem item2 = new MenuItem(menu, SWT.PUSH);
//				item2.setText("Opcion 2");
//				MenuItem item3 = new MenuItem(menu, SWT.PUSH);
//				item3.setText("Opcion 3");
//				menu.setLocation(viewer.toDisplay(e.x, e.y));
//				menu.setVisible(true);
//			}
//		});
	}

	private void changeSelectionMode(int newMode) {
		String[] layers = viewer.getAllIDLayers();

		switch (mode) {
		case MODE_ZOOMIN:
			zoomInItem.setSelection(false);
			break;
		case MODE_PAN:
			panItem.setSelection(false);
			break;
		case MODE_LINK:
			selItem.setSelection(false);
			break;
		case MODE_POLYLINE:
			if (newMode == MODE_POLYLINE) {
				viewer.endDraw();
				viewer.setModeLink(layers[layers.length-1]);
				newMode = MODE_NONE;
			}
			else {
				polylineItem.setSelection(false);
				viewer.cancelDraw();
			}
			break;
		case MODE_POLYGON:
			if (newMode == MODE_POLYGON) {
				viewer.endDraw();
				viewer.setModeLink(layers[layers.length-1]);
				newMode = MODE_NONE;
			}
			else {
				polygonItem.setSelection(false);
				viewer.cancelDraw();
			}
			break;
		case MODE_POINT:
			pointItem.setSelection(false);
			break;
		}

		switch (newMode) {
		case MODE_ZOOMIN:
			viewer.setModeZoomIn();
			break;
		case MODE_PAN:
			viewer.setModePan();
			break;
		case MODE_LINK:
			System.out.println("Capas:");
			for (int i=0; i<layers.length; i++) System.out.println(layers[i]);
			viewer.setModeLink(layers[layers.length-1]);
			break;
		case MODE_POLYLINE:
			if (polylineItem.getSelection()) viewer.setModeDrawLine(layers[layers.length-1]);
			break;
		case MODE_POLYGON:
			if (polygonItem.getSelection()) viewer.setModeDrawPolygon(layers[layers.length-1]);
			break;
		case MODE_POINT:
			viewer.setModeDrawPoint(layers[layers.length-1]);
			break;
		}

		mode = newMode;
	}
	
	private void loadSVG() {
		FileDialog fd = new FileDialog(sShell, SWT.OPEN);
		if(fd == null ) return;
		String[] filterExt = {"*.svg"};
		fd.setFilterExtensions(filterExt);
		String ruta = fd.open();
		if (ruta != null) {
			File file = new File(ruta);
			try {
				URL svgurl = new URL("file", "", slashify(file.getAbsolutePath(), file.isDirectory()));
				viewer.loadSVGUrl(svgurl);
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	private void saveSVG() {
		FileDialog fd = new FileDialog(sShell, SWT.SAVE);
		if(fd == null ) return;
		String[] filterExt = {"*.svg"};
		fd.setFilterExtensions(filterExt);
		String ruta = fd.open();
		if (ruta != null) {
			File file = new File(ruta);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(file);
				viewer.serializeSVG2XML(fos);
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			finally {
				try {
					if (fos!=null) fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void loadImage() {
		FileDialog fd = new FileDialog(sShell, SWT.OPEN);
		if(fd == null ) return;
		String[] filterExt = {"*.png;*.jpg;*.jpeg"};
		fd.setFilterExtensions(filterExt);
		String ruta = fd.open();
		if (ruta != null) {
			File file = new File(ruta);
			try {
				URL imgurl = new URL("file", "", slashify(file.getAbsolutePath(), file.isDirectory()));
				viewer.loadImage(viewer.getSize().x-100, viewer.getSize().y-100, 100, 100, imgurl.toExternalForm());
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private String slashify(String absolutePath, boolean directory) {
		String p = absolutePath;
		if (File.separatorChar != '/')
		{
			p = p.replace(File.separatorChar, '/');
		}
		if (!p.startsWith("/"))
		{
			p = "/" + p;
		}
		if (!p.endsWith("/") && directory)
		{
			p = p + "/";
		}
		return p;
	}
	
//	private void ordenCapas() {
//		Vector layers = new Vector();
//		layers.addElement(new LayerVisibility("Parcelas Industriales", true));
//		layers.addElement(new LayerVisibility("Parcelas Verdes", true));
//		layers.addElement(new LayerVisibility("Parcelas Varios", false));
//		layers.addElement(new LayerVisibility("Parcelas Dotacionales", true));
//		layers.addElement(new LayerVisibility("Naves", true));
//		layers.addElement(new LayerVisibility("Infraestructuras", true));
//		layers.addElement(new LayerVisibility("Fases", true));
//		controller.setLayerOrderAndVisibility(layers);
//	}
	
	public static void main(String[] args) {
		InputStream is = null;
		try {
			Properties props = new Properties();
			props.setProperty("log4j.rootLogger", "DEBUG, A1");
			props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
			props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.appender.A1.layout.ConversionPattern", "[%d] [%p] [%c{1}.%M()] - %m%n");
			props.setProperty("", "");
			//is = TestSVGViewer.class.getResourceAsStream("/log4j.properties");
			//props.load(is);
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { 
			if (is != null) is.close();
		} catch (Exception e) {}
		
        final TestSVGViewer test = new TestSVGViewer();
        
        // Controlador
        test.controller = new SVGLocalGISController(test.viewer);
        test.controller.setBrightness(0.8f);
        test.controller.setContrast(0.8f);
        
        test.viewer.addSVGViewerErrorListener(new SVGViewerErrorListener() {
			public void error(SVGViewerErrorEvent e) {
				System.out.println("Error: " + e);
			}
        });

		test.viewer.addSVGViewerLinkListener(new SVGViewerLinkListener() {
			public void nodeSelected(SVGNode n, Point point) {
				System.out.println("Nodo seleccionado: " + n);
				//test.viewer.moveNode(node.parent, node, node.parent.children.count-1);
			}
        });
		test.sShell.setVisible(true);
		while (!test.sShell.isDisposed()) {
			if (!test.display.readAndDispatch())
				test.display.sleep();
		}
		test.display.dispose();
		System.exit(0);
	}
}
