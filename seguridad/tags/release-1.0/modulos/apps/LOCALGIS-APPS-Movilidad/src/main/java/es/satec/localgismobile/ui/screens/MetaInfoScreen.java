package es.satec.localgismobile.ui.screens;

import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.geopista.feature.GeopistaSchema;
import com.tinyline.svg.SVGGroupElem;
import com.tinyline.svg.SVGNode;

import es.satec.localgismobile.fw.Global;
import es.satec.localgismobile.ui.LocalGISApplicationBaseWindow;
import es.satec.svgviewer.localgis.MetaInfo;

public class MetaInfoScreen extends LocalGISApplicationBaseWindow {

	private Composite composite;
	
	private static Logger logger = Global.getLoggerFor(MetaInfoScreen.class);
	
	public MetaInfoScreen(Shell parent, SVGNode currentNode, MetaInfo metaInfo, GeopistaSchema metaInfoSchema, Hashtable params) {
		super(parent, currentNode, metaInfo, metaInfoSchema, params);
		init();
		show();
	}

	private void init() {
		shell.setLayout(new FillLayout());
		goList();
	}
	
	protected void goList() {
		if (composite != null && !composite.isDisposed()) composite.dispose();
		
		composite = new MetaInfoListScreenComposite(this, currentNode, metaInfo, params);

		if (composite!=null && !composite.isDisposed()) {
			composite.setSize(shell.getClientArea().width, shell.getClientArea().height);
		}
	}
	
	protected void goDetail(SVGNode selectedMetaInfoNode, SVGNode parent,boolean newElement,boolean editable) {
		if (composite != null && !composite.isDisposed()) composite.dispose();

		//Componemos la pantalla con la informacion de metadatos.
		composite = new MetaInfoDetailScreenComposite(this, 
					selectedMetaInfoNode, parent, metaInfoSchema,
					newElement,editable, ((SVGGroupElem)currentNode.parent).getSystemId());

		if (composite!=null && !composite.isDisposed()) {
			composite.setSize(shell.getClientArea().width, shell.getClientArea().height);
		}
	}
	
}
