package es.satec.svgviewer.localgis.sld;

public class DeferredSymbolizer {

	private Symbolizer symbolizer;
	private int devX;
	private int devY;
	private String text;
	
	public DeferredSymbolizer(Symbolizer symbolizer, int devX, int devY, String text) {
		this.symbolizer = symbolizer;
		this.devX = devX;
		this.devY = devY;
		this.text = text;
	}
	
	public DeferredSymbolizer(Symbolizer symbolizer, int devX, int devY) {
		this(symbolizer, devX, devY, null);
	}

	public int getDevX() {
		return devX;
	}

	public void setDevX(int devX) {
		this.devX = devX;
	}

	public int getDevY() {
		return devY;
	}

	public void setDevY(int devY) {
		this.devY = devY;
	}

	public Symbolizer getSymbolizer() {
		return symbolizer;
	}

	public void setSymbolizer(Symbolizer symbolizer) {
		this.symbolizer = symbolizer;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
}
