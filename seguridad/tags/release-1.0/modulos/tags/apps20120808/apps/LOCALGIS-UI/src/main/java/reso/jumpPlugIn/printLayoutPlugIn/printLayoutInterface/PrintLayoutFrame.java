/*
 * Package reso.JumpPlugIn.PrintLayoutPlugIn.PrintLayoutInterface pour JUMP
 * Copyright (C) 2004 Olivier Bedel, ingénieur informaticien laboratoire Reso
 * UMR ESO 6590, Bassin versant du Jaudy-Guindy-Bizien. Céline Foureau,
 * stagiaire MIAGE, laboratoire Reso UMR ESO 6590. Erwan Bocher, doctorant en
 * géographie, laboratoire Reso UMR ESO 6590, Bassin versant du
 * Jaudy-Guindy-Bizien Date de création : 31 août 2004 Développé dans le cadre
 * du Projet APARAD (Laboratoire Reso UMR ESO 6590 CNRS / Bassin Versant du
 * Jaudy-Guindy-Bizien) Responsable : Erwan BOCHER Développeurs : Céline
 * FOUREAU, Olivier BEDEL olivier.bedel@uhb.fr ou olivier.bedel@yahoo.fr
 * erwan.bocher@uhb.fr ou erwan.bocher@free.fr celine.foureau@uhb.fr ou
 * celine.foureau@wanadoo.fr Ce package hérite de la licence GPL de JUMP. Il est
 * régi par la licence CeCILL soumise au droit français et respectant les
 * principes de diffusion des logiciels libres. (http://www.cecill.info)
 */

package reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import reso.jumpPlugIn.printLayoutPlugIn.I18NPlug;
import reso.jumpPlugIn.printLayoutPlugIn.PrintLayoutPlugIn;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.GraphicElements;
import reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.elements.graphicText.GraphicText;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.print.elements.ExtentManager;
import com.geopista.ui.plugin.print.elements.MapFrame;
import com.vividsolutions.jump.util.Blackboard;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.LayerViewPanelContext;
import com.vividsolutions.jump.workbench.ui.WorkbenchFrame;
import com.vividsolutions.jump.workbench.ui.images.IconLoader;

/**
 * @author FOUREAU_C
 */
public class PrintLayoutFrame extends JFrame
{
	public static final int			PAGE_ENTIERE	= 0;

	public static final int			LARGEUR_PAGE	= 1;

	private int						zoomActif		= PAGE_ENTIERE;

	private PrinterJob				printJob		= PrinterJob
															.getPrinterJob();

	private PageFormat				pageFormat		= printJob.defaultPage();

	private PrintLayoutToolBar		printLayoutToolBar;

	private PrintLayoutPreviewPanel	printLayoutPreviewPanel;

	public GraphicElements			ajouter;

	private GraphicElements			select			= null;

	public JLayeredPane getPageLegende()
	{
	return (this.getPrintLayoutPreviewPanel().getPreviewPanel().getPage()
			.getPageForPrint());
	}

	public PrintLayoutFrame(PlugInContext context)
	{
	setExtentManager(new ExtentManager(context.getLayerViewPanel()));

	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setIconImage(IconLoader.icon(
			I18NPlug.get(PrintLayoutPlugIn.name, "JUMPWorkbench.app-icon.gif"))
			.getImage());
	setName(I18NPlug
			.get(PrintLayoutPlugIn.name,
					"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame.Name"));
	setTitle(I18NPlug
			.get(PrintLayoutPlugIn.name,
					"reso.jumpPlugIn.printLayoutPlugIn.printLayoutInterface.PrintLayoutFrame.Name"));
	setJMenuBar(new PrintLayoutMenuBar(this));
	this.setPlugInContext(context);
	this.setLayerViewPanelContext(context.getWorkbenchFrame());
	printLayoutToolBar = new PrintLayoutToolBar(this);
	getContentPane().add(printLayoutToolBar, BorderLayout.NORTH);
	printLayoutPreviewPanel = new PrintLayoutPreviewPanel(this);
	printLayoutPreviewPanel.setSize(new Dimension(875, 675));
	PreviewPanel preview = new PreviewPanel(this);
	printLayoutPreviewPanel.setPreview(preview);
	getContentPane().add(printLayoutPreviewPanel, BorderLayout.CENTER);
	setMapsExtents();
	pack();
	centrer();
	show();
	}

	public PrintLayoutToolBar getToolBar()
	{
	return printLayoutToolBar;
	}

	public PrinterJob getPrinterJob()
	{
	return printJob;
	}

	public PageFormat getPageFormat()
	{
	return pageFormat;
	}

	public void setPageFormat(PageFormat pageFormat)
	{
	this.pageFormat = pageFormat;
	}

	public PrintLayoutPreviewPanel getPrintLayoutPreviewPanel()
	{
	return printLayoutPreviewPanel;
	}

	public int getZoomActif()
	{
	return zoomActif;
	}

	public void setZoomActif(int zoom)
	{
	zoomActif = zoom;
	}

	public void setAjouter(GraphicElements component)
	{
	ajouter = component;
	}

	public GraphicElements getAjout()
	{
	return ajouter;
	}

	public void initAjout()
	{
	ajouter = null;
	}

	public GraphicElements getSelectedComponent()
	{
	return select;
	}

	public void setSelectedComponent(GraphicElements component)
	{
	select = component;
	if (select != null)
		{
		printLayoutToolBar.getDeleteButton().setEnabled(true);
		printLayoutToolBar.getUpbutton().setEnabled(true);
		printLayoutToolBar.getDownbutton().setEnabled(true);
		}
	else
		{
		printLayoutToolBar.getDeleteButton().setEnabled(false);
		printLayoutToolBar.getUpbutton().setEnabled(false);
		printLayoutToolBar.getDownbutton().setEnabled(false);
		}
	}

	private LayerViewPanelContext	layerViewPanelContext;

	private PlugInContext	context;

	private ExtentManager	extentManager;

	public void centrer()
	{
	// On récupère la taille de l'écran (la résolution)
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	// et on place notre fenêtre au milieu
	setLocation((screen.width - getSize().width) / 2,
			(screen.height - getSize().height) / 2);
	}

	public void setLayerViewPanelContext(LayerViewPanelContext wbf)
	{
	this.layerViewPanelContext = wbf;
	}

	public LayerViewPanelContext getLayerViewPanelContext()
	{
	return layerViewPanelContext;
	}

	public void setPlugInContext(PlugInContext context)
	{
	this.context = context;
	}

	public PlugInContext getPlugInContext()
	{
	return context;
	}

	public Page getPage()
	{
	return (this.getPrintLayoutPreviewPanel().getPreviewPanel().getPage());
	}

	/**
	 * 
	 */
	public void popSelected()
	{
	GraphicElements gesel = getSelectedComponent();
	if (gesel != null) getPage().toFront(gesel);
	}

	/**
	 * 
	 */
	public void pushSelected()
	{
	GraphicElements gesel = getSelectedComponent();
	if (gesel != null) getPage().toBack(gesel);
	}

	public void setExtentManager(ExtentManager extentManager)
	{
	this.extentManager = extentManager;
	}

	public ExtentManager getExtentManager()
	{
	return extentManager;
	}
	/**
	 * Pasa a la siguiente página
	 */
	int	currentPage	= 0;

	public void nextPage()
	{
	int extentCount = getExtentManager().getExtentCount();
	int currentPage=getCurrentPage();
	//YR--
	if (extentCount > 1 && extentCount > currentPage + 1)
		{
		setCurrentPage(currentPage+1);
		setMapsExtents();
		}
	}

	/**
	 * 
	 */
	public void setMapsExtents()
	{
	List elements = getPage().getGraphicElement();
	for (Iterator iter = elements.iterator(); iter.hasNext();)
		{
		GraphicElements element = (GraphicElements) iter.next();
		if (element instanceof MapFrame)
			{
			MapFrame theMap = (MapFrame) element;
			theMap.initZoom();
			}
		else if (element instanceof GraphicText)
			{
			//updates texts allowing substitutions including page placeholder
			GraphicText text = (GraphicText) element;
			Blackboard bb=AppContext.getApplicationContext().getBlackboard();
			bb.put(com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_PAGECOUNT,
					Integer.toString(getExtentManager().getExtentCount()));
			bb.put(com.geopista.ui.plugin.print.PrintLayoutPlugIn.GEOPISTA_PRINT_CURRENTPAGE,
					Integer.toString(getCurrentPage()+1));
			text.setText(text.getText());				
			}
		}
	}

	/**
	 * 
	 */
	public void prevPage()
	{
	int currentPage=getCurrentPage();
	int extentCount = getExtentManager().getExtentCount();
	if (extentCount > 1 && 0 < currentPage)
		{
		setCurrentPage(currentPage-1);
		setMapsExtents();
		}
	}

	public int getCurrentPage()
	{
	return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
	this.currentPage = currentPage;
	getExtentManager().setCurrentExtent(currentPage);
	}
}