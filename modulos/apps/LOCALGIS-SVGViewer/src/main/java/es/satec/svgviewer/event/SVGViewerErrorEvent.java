/**
 * SVGViewerErrorEvent.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.svgviewer.event;

/**
 * Evento para notificar un error en el visor.
 * @author jpresa
 */
public class SVGViewerErrorEvent {
	
	private String errorMessage;
	private Throwable throwable;

	public SVGViewerErrorEvent(String errorMessage, Throwable throwable) {
		this.errorMessage = errorMessage;
		this.throwable = throwable;
	}
	
	public SVGViewerErrorEvent(String errorMessage) {
		this(errorMessage, null);
	}
	
	public SVGViewerErrorEvent(Throwable throwable) {
		this(throwable.getMessage(), throwable);
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	public String toString() {
		return errorMessage;
	}
}
