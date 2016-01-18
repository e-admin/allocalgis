/**
 * SHPLayersScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

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
import es.satec.svgviewer.localgis.shp.ShpException;
import es.satec.svgviewer.localgis.shp.Theme;

public class SHPLayersScreen extends LocalGISWindow {
	
	private SVGLocalGISViewer viewer;
	
	private Vector checkboxes;
	
	private boolean redraw;
	
	private int added;
	
	private Color newColor;
	
	private ScrolledComposite scrolledComposite;
	private Composite composite;
	
	private static Logger logger = Global.getLoggerFor(SHPLayersScreen.class);
	
	public SHPLayersScreen(Shell parent, SVGLocalGISViewer viewer) {
		super(parent);
		this.viewer = viewer;
		checkboxes = new Vector();
		redraw = false;
		added = 0;
		newColor = null;
		init();
		show();
	}

	private void init() {
		GridLayout gridLayout = new GridLayout();
		shell.setText(Messages.getMessage("SHPLayersScreen_Titulo"));
		shell.setLayout(gridLayout);
		shell.setBackground(Config.COLOR_APLICACION);

		Label labelHeader = new Label(shell, SWT.WRAP);
		labelHeader.setBackground(Config.COLOR_APLICACION);
		labelHeader.setText(Messages.getMessage("SHPLayersScreen_Header"));
		
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
		
		// Cargar datos
		Vector shps = viewer.getLoadedShps();
		if (shps != null) {
			Enumeration en = shps.elements();
			while (en.hasMoreElements()) {
				Theme theme = (Theme) en.nextElement();
				
				Button checkboxSHP = new Button(composite, SWT.CHECK);
				checkboxSHP.setBackground(Config.COLOR_APLICACION);
				checkboxSHP.setSelection(theme.enable);
				checkboxSHP.setData(theme);
				checkboxes.addElement(checkboxSHP);
				
				Label labelSHP = new Label(composite, SWT.NONE);
				labelSHP.setBackground(Config.COLOR_APLICACION);
				labelSHP.setText(getFileName(theme.filePath));
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
		Composite compositeColor = new Composite(shell, SWT.NONE);
		compositeColor.setBackground(Config.COLOR_APLICACION);
		FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 5;
		compositeColor.setLayout(fillLayout);
		final Label labelColor = new Label(compositeColor, SWT.NONE);
		labelColor.setBackground(viewer.getShpReader().getForeColor());
		labelColor.setText("    ");
		Button buttonColor = new Button(compositeColor, SWT.PUSH);
		buttonColor.setText(Messages.getMessage("SHPLayersScreen_Color"));
		
		buttonColor.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			public void widgetSelected(SelectionEvent arg0) {
		        // Create the color-change dialog
		        ColorDialog dlg = new ColorDialog(shell);

		        // Set the selected color in the dialog from
		        // user's selected color
		        Color shpColor = viewer.getShpReader().getForeColor();
		        dlg.setRGB(shpColor.getRGB());

		        // Change the title bar text
		        dlg.setText(Messages.getMessage("SHPLayersScreen_ColorDialog"));

		        // Open the dialog and retrieve the selected color
		        RGB rgb = dlg.open();
		        if (rgb != null && !rgb.equals(shpColor.getRGB())) {
		          // Dispose the old color, create the
		          // new one, and set into the label
		        	newColor = new Color(shell.getDisplay(), rgb);
					labelColor.setBackground(newColor);
		        }
			}
		});
		
		GridData gdt = new GridData();
		gdt.horizontalAlignment = GridData.CENTER;
		gdt.grabExcessHorizontalSpace = true;
		gdt.verticalAlignment = GridData.CENTER;
		ToolBar toolBarButtons = new ToolBar(shell, SWT.NONE);
		toolBarButtons.setBackground(Config.COLOR_APLICACION);
		toolBarButtons.setLayoutData(gdt);
		
		InputStream isAdd = null;
		Image imAdd = null;
		InputStream isAccept = null;
		Image imAccept = null;
		InputStream isCancel = null;
		Image imCancel = null;
		try {
			ToolItem toolItemAdd = new ToolItem(toolBarButtons, SWT.PUSH);
			String imPathAdd = Config.prResources.getProperty("SHPLayersScreen_add");
			isAdd = this.getClass().getClassLoader().getResourceAsStream(imPathAdd);
			imAdd = new Image(Display.getCurrent(), isAdd);
			toolItemAdd.setImage(imAdd);
			toolItemAdd.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					
					FileDialog fd = new FileDialog(shell, SWT.OPEN);
					if(fd == null) return;
					String[] filterExt = {"*.shp"};
					fd.setFilterExtensions(filterExt);
					String shpFilePath = fd.open();
					if (shpFilePath != null) {
						ScreenUtils.startHourGlass(shell);

						try {
							Theme theme = viewer.addShp(shpFilePath);
					
							Button checkboxSHP = new Button(composite, SWT.CHECK);
							checkboxSHP.setBackground(Config.COLOR_APLICACION);
							checkboxSHP.setSelection(theme.enable);
							checkboxSHP.setData(theme);
							checkboxes.addElement(checkboxSHP);
						
							Label labelSHP = new Label(composite, SWT.NONE);
							labelSHP.setBackground(Config.COLOR_APLICACION);
							labelSHP.setText(getFileName(theme.filePath));
							
							Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
							composite.setSize(pSize);
							scrolledComposite.setMinSize(pSize);
					
							redraw = true;
							added++;

							// Comparar el bounding box del svg y el shp. Si son disjuntos, es
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
							
							double shpScale = viewer.getShpReader().getScale();
							BoundingBox bboxSHP = new BoundingBox(theme.theme_box.min.dx/shpScale, theme.theme_box.min.dy/shpScale,
									theme.theme_box.max.dx/shpScale, theme.theme_box.max.dy/shpScale);
							logger.debug("Bounding box SHP: " + bboxSHP);
							
							ScreenUtils.stopHourGlass(shell);

							if (!bboxSVG.intersects(bboxSHP)) {
								MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
								mb.setMessage(Messages.getMessage("SHPLayersScreen_SRSWarning"));
								mb.open();
							}

						} catch (ShpException e) {
							ScreenUtils.stopHourGlass(shell);
							MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
							mb.setMessage(Messages.getMessage("SHPLayersScreen_LoadError"));
							mb.open();
						}
					}
				}
			});

			ToolItem toolItemAccept = new ToolItem(toolBarButtons, SWT.PUSH);
			String imPathAccept = Config.prResources.getProperty("SHPLayersScreen_accept");
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
						Theme theme = (Theme) cb.getData();
						if (theme.enable != cb.getSelection()) {
							theme.enable = cb.getSelection();
							redraw = true;
						}
					}
					
					if (newColor != null) {
						viewer.getShpReader().setColor(newColor);
						redraw = true;
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
			String imPathCancel = Config.prResources.getProperty("SHPLayersScreen_cancel");
			isCancel = this.getClass().getClassLoader().getResourceAsStream(imPathCancel);
			imCancel = new Image(Display.getCurrent(), isCancel);
			toolItemCancel.setImage(imCancel);
			toolItemCancel.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					for (int i=viewer.getLoadedShps().size()-1; added>0; added--, i--) {
						viewer.removeShp(i);
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
}
