package es.satec.svgviewer;

public class SVGLoadImageEventData {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private String path;
	
	public SVGLoadImageEventData(int x, int y, int width, int height, String path) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
