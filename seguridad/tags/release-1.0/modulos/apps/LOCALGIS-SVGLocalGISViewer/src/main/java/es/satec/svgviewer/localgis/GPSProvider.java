package es.satec.svgviewer.localgis;

public interface GPSProvider {

	public void startGPS() throws Exception;
	
	public void stopGPS();
	
	public GPSLocation getGPSLocation();
	
	public boolean isStarted();
	
}
