package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
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
import org.satec.sld.SVG.SVGNodeFeature;

import com.geopista.feature.Domain;
import com.geopista.feature.GeopistaSchema;
import com.geopista.feature.GeopistaSchemaFactory;
import com.tinyline.svg.SVGNode;

import es.satec.svgviewer.event.SVGViewerLinkListener;
import es.satec.svgviewer.localgis.SVGLocalGISController;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;
import es.satec.svgviewer.localgis.WMSData;

public class TestSVGLocalGISViewer {

	private Display display = null;
	private Shell sShell = null;

	private ToolBar toolBar = null;
	private ToolItem fileItem = null;
	private ToolItem zoomInItem = null;
	private ToolItem zoomOutItem = null;
	private ToolItem panItem = null;
	private ToolItem gpsItem = null;
	private ToolItem selItem = null;
	private ToolItem ortoItem = null;
	private ToolItem sldItem = null;
	private ToolItem schItem = null;

	private boolean ortofotoActiva = false;
	private boolean estilosActivos = false;

	private SVGLocalGISViewer viewer;

	private int mode;
	public static final int MODE_NONE = 0;
	public static final int MODE_ZOOMIN = 1;
	public static final int MODE_PAN = 2;
	public static final int MODE_SEL = 3;

	public TestSVGLocalGISViewer() {
		mode = MODE_NONE;
		ortofotoActiva = false;
		display = new Display();
		createSShell();
	}

	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.CLOSE);
		sShell.setText("SVG LocalGIS Viewer");
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

//		gpsItem = new ToolItem(toolBar, SWT.PUSH);
//		gpsItem.setText("GPS");
//		gpsItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				if (!viewer.goGPS()) {
//					MessageBox mb = new MessageBox(sShell, SWT.ICON_WARNING | SWT.OK);
//					mb.setMessage("No hay posicion GPS");
//					mb.open();
//				}
//			}
//			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
//			}
//		});

		selItem = new ToolItem(toolBar, SWT.RADIO);
		selItem.setText("Sel");
		selItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				changeSelectionMode(MODE_SEL);
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		ortoItem = new ToolItem(toolBar, SWT.CHECK);
		ortoItem.setText("Orto");
		ortoItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (ortofotoActiva) {
					viewer.removeAllMapServers();
					ortofotoActiva = false;
				}
				else {
					// Servidores de mapas
					viewer.addMapServer(new WMSData("http://wms.mapa.es/wms/wms.aspx", "1.1.0", "ORTOFOTOS", "", "EPSG:23030", "image/jpeg", true));
					viewer.addMapServer(new WMSData("http://ovc.catastro.meh.es/Cartografia/WMS/ServidorWMS.aspx", "1.1.1",
					"Catastro,CONSTRU,TXTCONSTRU,SUBPARCE,TXTSUBPARCE,PARCELA,TXTPARCELA,MASA,TXTMASA,EJES,LIMITES,TEXTOS,ELEMLIN",
					"", "EPSG:23030", "image/png", true));
					ortofotoActiva = true;
				}
				viewer.reloadCurrentZone();
				viewer.drawSVG();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

		sldItem = new ToolItem(toolBar, SWT.CHECK);
		sldItem.setText("SLD");
		sldItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (estilosActivos) {
					viewer.setLayerSLDActive("Parcelas", false);
					viewer.setSearchSLDActive(false);
					estilosActivos = false;
				}
				else {
					viewer.setLayerSLDActive("Parcelas", true);
					viewer.setSearchSLDActive(true);
					estilosActivos = true;
				}
				viewer.drawSVG();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});

//		schItem = new ToolItem(toolBar, SWT.PUSH);
//		schItem.setText("SCH");
//		schItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
//			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
//				testSCH();
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
		viewer = new SVGLocalGISViewer(sShell, SWT.NO_BACKGROUND);
		viewer.setLayoutData(gridData);
		viewer.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	}

	private void changeSelectionMode(int newMode) {
		switch (mode) {
		case MODE_ZOOMIN:
			zoomInItem.setSelection(false);
			break;
		case MODE_PAN:
			panItem.setSelection(false);
			break;
		case MODE_SEL:
			selItem.setSelection(false);
			break;
		}

		switch (newMode) {
		case MODE_ZOOMIN:
			viewer.setModeZoomIn();
			break;
		case MODE_PAN:
			viewer.setModePan();
			break;
		case MODE_SEL:
			viewer.setModeLink("Parcelas");
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
viewer.loadSLD(new FileInputStream("\\parcelaurbana.xml"), "Parcelas", "parcelasurbana:_:TematicoParcelas", false, false);
//viewer.loadSLD(new FileInputStream("\\busqueda.xml"), "Parcelas", "busqueda", false, true);
				viewer.loadSVGUrl(svgurl);
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

	/**
	 * Metodo de prueba de los sch.
	 *
	 */
	private void testSCH() {
		try {
			SVGNode layerParcelas = (SVGNode) viewer.getSVGDocument().root.children.data[0];
	
			GeopistaSchema geopistaSchema = GeopistaSchemaFactory.loadGeopistaSchema(new FileInputStream("\\Tramos de abastecimiento.sch"/*"\\Parcelas.sch"*/));
			Domain domain = geopistaSchema.getAttributeDomain("Tipo material"/*"Referencia catastral"*/);
			
			int validas1 = 0;
			int validas2 = 0;
			for (int i =0 ;i < layerParcelas.children.count; i++){
				SVGNode node = (SVGNode) layerParcelas.children.data[i];
				SVGNodeFeature feature = new SVGNodeFeature (node);
				if (domain.validate(feature, "Codigo Delegacin MEH"/*"www"*/, null)) validas1++;
				if (domain.validate(null, null, feature.getAttribute("Codigo Delegacin MEH"/*"www"*/))) validas2++;
			}
			System.out.println("Parcelas evaluadas: " + layerParcelas.children.count);
			System.out.println("Validas con el metodo 1: " + validas1);
			System.out.println("Validas con el metodo 2: " + validas2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Properties props = new Properties();
			props.setProperty("log4j.rootLogger", "DEBUG, A1");
			props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
			props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.appender.A1.layout.ConversionPattern", "[%d] [%p] [%c{1}.%M()] - %m%n");
			props.setProperty("", "");
			PropertyConfigurator.configure(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { 
			final TestSVGLocalGISViewer test = new TestSVGLocalGISViewer();
	
			test.viewer.setGpsActive(true);
			test.viewer.setGPSTrackingActive(true);
	
			// Controlador
			SVGLocalGISController controller = new SVGLocalGISController(test.viewer);
			controller.setBrightness(0.8f);
			controller.setContrast(0.8f);
	
			test.sShell.setVisible(true);
			while (!test.sShell.isDisposed()) {
				if (!test.display.readAndDispatch())
					test.display.sleep();
			}
			test.viewer.setGpsActive(false);
			test.display.dispose();
	
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
