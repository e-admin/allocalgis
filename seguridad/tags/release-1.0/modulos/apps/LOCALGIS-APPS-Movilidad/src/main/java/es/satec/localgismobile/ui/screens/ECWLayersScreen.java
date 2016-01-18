package es.satec.localgismobile.ui.screens;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.ermapper.ecw.JNCSException;
import com.ermapper.ecw.JNCSFile;
import com.tinyline.svg.SVGDocument;
import com.tinyline.tiny2d.TinyRect;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.svgviewer.localgis.BoundingBox;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;

public class ECWLayersScreen extends LocalGISWindow {
	
	private SVGLocalGISViewer viewer;
	
	private Vector checkboxes;
	
	private boolean redraw;
	
	private int added;
	
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	
	Image imAdd = null;
	Image imAccept = null;
	Image imCancel = null;
	
	private static Logger logger = Global.getLoggerFor(ECWLayersScreen.class);
	
	public ECWLayersScreen(Shell parent, SVGLocalGISViewer viewer) {
		super(parent);
		this.viewer = viewer;
		checkboxes = new Vector();
		redraw = false;
		added = 0;
		init();
		show();
	}

	private void init() {
		GridLayout gridLayout = new GridLayout();
		shell.setText(Messages.getMessage("ECWLayersScreen_Titulo"));
		shell.setLayout(gridLayout);
		shell.setBackground(Config.COLOR_APLICACION);

		Label labelHeader = new Label(shell, SWT.WRAP);
		labelHeader.setBackground(Config.COLOR_APLICACION);
		labelHeader.setText(Messages.getMessage("ECWLayersScreen_Header"));
		
		createScrolledComposite();

		createButtons();
	}
	
	private void createScrolledComposite() {
		scrolledComposite=new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL|SWT.V_SCROLL);
		scrolledComposite.setBackground(Config.COLOR_APLICACION);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setLayout(new GridLayout());		
		scrolledComposite.setExpandHorizontal(true);
        GridData gdsc=new GridData();
        gdsc.grabExcessHorizontalSpace = true;
        gdsc.verticalAlignment = GridData.FILL;
        gdsc.horizontalAlignment = GridData.FILL;
        gdsc.grabExcessVerticalSpace = true;
        scrolledComposite.setLayoutData(gdsc);
        scrolledComposite.setLayout(new FillLayout());
        
		GridLayout glc = new GridLayout();
		glc.numColumns = 2;
		composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(Config.COLOR_APLICACION);
		composite.setLayout(glc);
		
		composite.addDisposeListener(new DisposeListener() {
		      public void widgetDisposed(DisposeEvent e) {
		        dispose();
		      }
		});

		
		// Cargar datos
		Vector ecws = viewer.getLoadedEcws();
		if (ecws != null) {
			Enumeration en = ecws.elements();
			while (en.hasMoreElements()) {
				JNCSFile file = (JNCSFile) en.nextElement();
				
				Button checkboxECW = new Button(composite, SWT.CHECK);
				checkboxECW.setBackground(Config.COLOR_APLICACION);
				checkboxECW.setSelection(file.isVisible());
				checkboxECW.setData(file);
				checkboxes.addElement(checkboxECW);
				
				Label labelECW = new Label(composite, SWT.NONE);
				labelECW.setBackground(Config.COLOR_APLICACION);
				labelECW.setText(getFileName(file.fileName));
			}
		}
        
        scrolledComposite.setContent(composite);

		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
	}
	
	private String getFileName(String filePath) {
		File f = new File(filePath);
		if (f.exists())
			return f.getName();
		else
			return "";
	}
	
	private void createButtons() {
		GridData gdt = new GridData();
		gdt.horizontalAlignment = GridData.CENTER;
		gdt.grabExcessHorizontalSpace = true;
		gdt.verticalAlignment = GridData.CENTER;
		ToolBar toolBarButtons = new ToolBar(shell, SWT.NONE);
		toolBarButtons.setBackground(Config.COLOR_APLICACION);
		toolBarButtons.setLayoutData(gdt);
		
		InputStream isAdd = null;
		InputStream isAccept = null;
		InputStream isCancel = null;
		try {
			ToolItem toolItemAdd = new ToolItem(toolBarButtons, SWT.PUSH);
			String imPathAdd = Config.prResources.getProperty("ECWLayersScreen_add");
			isAdd = this.getClass().getClassLoader().getResourceAsStream(imPathAdd);
			imAdd = new Image(Display.getCurrent(), isAdd);
			toolItemAdd.setImage(imAdd);
			toolItemAdd.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					
					FileDialog fd = new FileDialog(shell, SWT.OPEN);
					if(fd == null) return;
					String[] filterExt = {"*.ecw"};
					fd.setFilterExtensions(filterExt);
					String ecwFilePath = fd.open();
					if (ecwFilePath != null) {
						ScreenUtils.startHourGlass(shell);

						try {
							JNCSFile file = viewer.addEcw(ecwFilePath);
							
							// Solo se soportan las unidades en metros
							if (file.cellSizeUnits != JNCSFile.ECW_CELL_UNITS_METERS) {
								viewer.removeEcw(viewer.getLoadedEcws().size()-1);

								ScreenUtils.stopHourGlass(shell);
								MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
								mb.setMessage(Messages.getMessage("ECWLayersScreen_UnsupportedSRS"));
								mb.open();
								return;
							}
					
							Button checkboxECW = new Button(composite, SWT.CHECK);
							checkboxECW.setBackground(Config.COLOR_APLICACION);
							checkboxECW.setSelection(file.isVisible());
							checkboxECW.setData(file);
							checkboxes.addElement(checkboxECW);
						
							Label labelECW = new Label(composite, SWT.NONE);
							labelECW.setBackground(Config.COLOR_APLICACION);
							labelECW.setText(getFileName(file.fileName));
							
							Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
							composite.setSize(pSize);
							scrolledComposite.setMinSize(pSize);
					
							redraw = true;
							added++;

							// Comparar el bounding box del svg y el ecw. Si son disjuntos, es
							// probable que se estén usando diferentes sistemas de coordenadas
							SVGDocument svgDoc = viewer.getSVGDocument();
							double despX = 0.0;
							double despY = 0.0;
							if (svgDoc.root.nameAtts != null && svgDoc.root.nameAtts.size() == 2) {
								despX = Double.parseDouble((String) svgDoc.root.nameAtts.elementAt(0));
								despY = Double.parseDouble((String) svgDoc.root.nameAtts.elementAt(1));
							}
							TinyRect origview = new TinyRect(svgDoc.renderer.origview.x, svgDoc.renderer.origview.y,
								svgDoc.renderer.origview.x + svgDoc.renderer.origview.width, svgDoc.renderer.origview.y + svgDoc.renderer.origview.height);
							BoundingBox bboxSVG = new BoundingBox(despX+((float)origview.xmin/256f), despY-((float)origview.ymax/256f),
									despX+((float)origview.xmax/256f), despY-((float)origview.ymin/256f));
							
							logger.debug("Bounding box SVG: " + bboxSVG);
							
							BoundingBox bboxECW = new BoundingBox(file.originX, file.originY+file.cellIncrementY*file.height,
									file.originX+file.cellIncrementX*file.width, file.originY);
							logger.debug("Bounding box ECW: " + bboxECW);
							
							ScreenUtils.stopHourGlass(shell);

							if (!bboxSVG.intersects(bboxECW)) {
								MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
								mb.setMessage(Messages.getMessage("ECWLayersScreen_SRSWarning"));
								mb.open();
							}

						} catch (JNCSException e) {
							ScreenUtils.stopHourGlass(shell);
							MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(Messages.getMessage("ECWLayersScreen_LoadError"));
							mb.open();
						}
					}
				}
			});

			ToolItem toolItemAccept = new ToolItem(toolBarButtons, SWT.PUSH);
			String imPathAccept = Config.prResources.getProperty("ECWLayersScreen_accept");
			isAccept = this.getClass().getClassLoader().getResourceAsStream(imPathAccept);
			imAccept = new Image(Display.getCurrent(), isAccept);
			toolItemAccept.setImage(imAccept);
			toolItemAccept.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					Enumeration en = checkboxes.elements();
					while (en.hasMoreElements()) {
						Button cb = (Button) en.nextElement();
						JNCSFile file = (JNCSFile) cb.getData();
						if (file.isVisible() != cb.getSelection()) {
							file.setVisible(cb.getSelection());
							redraw = true;
						}
					}
					
					ScreenUtils.startHourGlass(shell);
					if (redraw) {
						viewer.reloadCurrentZone();
						viewer.drawSVG();
					}
					ScreenUtils.stopHourGlass(shell);
					shell.close();
				}
			});
			
			ToolItem toolItemCancel = new ToolItem(toolBarButtons, SWT.PUSH);
			String imPathCancel = Config.prResources.getProperty("ECWLayersScreen_cancel");
			isCancel = this.getClass().getClassLoader().getResourceAsStream(imPathCancel);
			imCancel = new Image(Display.getCurrent(), isCancel);
			toolItemCancel.setImage(imCancel);
			toolItemCancel.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					for (int i=viewer.getLoadedEcws().size()-1; added>0; added--, i--) {
						viewer.removeEcw(i);
					}
					shell.close();
				}
				
			});
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
				if (isAdd != null) isAdd.close();
				if (imAdd != null) imAdd.dispose();
				if (isAccept != null) isAccept.close();
				if (imAccept != null) imAccept.dispose();
				if (isCancel != null) isCancel.close();
				if (imCancel != null) imCancel.dispose();
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	public void dispose(){
		if ((imAdd!=null) && (!imAdd.isDisposed()))
			imAdd.dispose();
		if ((imAccept!=null) && (!imAccept.isDisposed()))
				imAccept.dispose();
		if ((imCancel!=null) && (!imCancel.isDisposed()))
				imCancel.dispose();
	}
}
