package es.satec.localgismobile.session;

import java.util.Enumeration;
import java.util.Vector;

import es.satec.svgviewer.localgis.MetaInfo;

public class CellInfo {
	
	private String selectedCell;
	private Vector metaInfos;

	public CellInfo() {
		
	}
	
	public CellInfo(String selectedCell, Vector metaInfos) {
		this.selectedCell = selectedCell;
		this.metaInfos = metaInfos;
	}

	public Vector getMetaInfos() {
		return metaInfos;
	}
	
	public MetaInfo getMetaInfo(String appName) {
		if (metaInfos == null) return null;
		Enumeration e = metaInfos.elements();
		while (e.hasMoreElements()) {
			MetaInfo mi = (MetaInfo) e.nextElement();
			if (mi.getInfoType().equals(appName)) {
				return mi;
			}
		}
		return null;
	}

	public void setMetaInfos(Vector metaInfos) {
		this.metaInfos = metaInfos;
	}

	public String getSelectedCell() {
		return selectedCell;
	}

	public void setSelectedCell(String selectedCell) {
		this.selectedCell = selectedCell;
	}

}
