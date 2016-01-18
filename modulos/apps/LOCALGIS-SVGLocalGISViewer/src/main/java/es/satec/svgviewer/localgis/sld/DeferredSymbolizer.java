/**
 * DeferredSymbolizer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
