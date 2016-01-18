/**
 * WMSLayersScreen.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.ui.screens;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import es.satec.localgismobile.fw.Config;
import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.fw.Messages;
import es.satec.localgismobile.ui.LocalGISWindow;
import es.satec.localgismobile.ui.utils.ScreenUtils;
import es.satec.localgismobile.ui.widgets.ScrolledComposite;
import es.satec.svgviewer.localgis.SVGLocalGISViewer;
import es.satec.svgviewer.localgis.WMSData;

public class WMSLayersScreen extends LocalGISWindow {
	
	private SVGLocalGISViewer viewer;
	
	private Vector checkboxes;
	
	private static Logger logger = Global.getLoggerFor(WMSLayersScreen.class);
	
	public WMSLayersScreen(Shell parent, SVGLocalGISViewer viewer) {
		super(parent);
		this.viewer=viewer;
		checkboxes = new Vector();
		init();
		show();
	}

	private void init() {
		GridLayout gridLayout = new GridLayout();
		shell.setText(Messages.getMessage("WMSLayersScreen_Titulo"));
		shell.setLayout(gridLayout);
		shell.setBackground(Config.COLOR_APLICACION);

		Label labelHeader = new Label(shell, SWT.WRAP);
		labelHeader.setBackground(Config.COLOR_APLICACION);
		labelHeader.setText(Messages.getMessage("WMSLayersScreen_Header"));
		
		createScrolledComposite();

		createButtons();
	}
	
	private void createScrolledComposite() {
		ScrolledComposite scrolledComposite=new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL|SWT.V_SCROLL);
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
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setBackground(Config.COLOR_APLICACION);
		composite.setLayout(glc);
		
		// Cargar datos
		Vector mapServers = viewer.getMapServers();
		if (mapServers != null) {
			Enumeration en = mapServers.elements();
			while (en.hasMoreElements()) {
				WMSData wmsData = (WMSData) en.nextElement();
				
				Button checkboxWMS = new Button(composite, SWT.CHECK);
				checkboxWMS.setBackground(Config.COLOR_APLICACION);
				checkboxWMS.setSelection(wmsData.isActive());
				checkboxWMS.setData(wmsData);
				checkboxes.addElement(checkboxWMS);
				
				Label labelWMS = new Label(composite, SWT.NONE);
				labelWMS.setBackground(Config.COLOR_APLICACION);
				labelWMS.setText(wmsData.getLayers());
			}
		}
        
        scrolledComposite.setContent(composite);

		Point pSize=composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(pSize);
		scrolledComposite.setMinSize(pSize);
	}
	
	private void createButtons() {
		GridData gdt = new GridData();
		gdt.horizontalAlignment = GridData.CENTER;
		gdt.grabExcessHorizontalSpace = true;
		gdt.verticalAlignment = GridData.CENTER;
		ToolBar toolBarButtons = new ToolBar(shell, SWT.NONE);
		toolBarButtons.setBackground(Config.COLOR_APLICACION);
		toolBarButtons.setLayoutData(gdt);
		
		InputStream isAccept = null;
		Image imAccept = null;
		InputStream isCancel = null;
		Image imCancel = null;
		try {
			ToolItem toolItemAccept = new ToolItem(toolBarButtons, SWT.PUSH);
			String imPathAccept = Config.prResources.getProperty("WMSLayersScreen_accept");
			isAccept = this.getClass().getClassLoader().getResourceAsStream(imPathAccept);
			imAccept = new Image(Display.getCurrent(), isAccept);
			toolItemAccept.setImage(imAccept);
			toolItemAccept.addSelectionListener(new SelectionListener(){
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					boolean redraw = false;
					Enumeration en = checkboxes.elements();
					while (en.hasMoreElements()) {
						Button cb = (Button) en.nextElement();
						WMSData wmsData = (WMSData) cb.getData();
						if (wmsData.isActive() != cb.getSelection()) {
							wmsData.setActive(cb.getSelection());
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
			String imPathCancel = Config.prResources.getProperty("WMSLayersScreen_cancel");
			isCancel = this.getClass().getClassLoader().getResourceAsStream(imPathCancel);
			imCancel = new Image(Display.getCurrent(), isCancel);
			toolItemCancel.setImage(imCancel);
			toolItemCancel.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent event) {
				}
	
				public void widgetSelected(SelectionEvent event) {
					shell.close();
				}
				
			});
		} catch (Exception e) {
			logger.error(e, e);
		} finally {
			try {
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
