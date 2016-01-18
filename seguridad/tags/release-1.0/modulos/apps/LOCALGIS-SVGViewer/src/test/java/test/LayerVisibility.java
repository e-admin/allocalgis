package test;

public class LayerVisibility {

	private String layerId;
	private boolean visible;

	/**
	 * @param layerId
	 * @param visibility
	 */
	public LayerVisibility(String layerId, boolean visible) {
		this.layerId = layerId;
		this.visible = visible;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public String toString() {
		return "id: " + layerId + ", visible: " + visible;
	}
}
