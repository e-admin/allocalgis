package com.geopista.ui.reports.mapprintpreviewer;

import com.vividsolutions.jts.geom.Envelope;

public interface MapPrintPreviewer {

	public Envelope getCurrentPrintEnvelope();
	
	public void updatePrintEnvelopeToCurrentScale();
}
