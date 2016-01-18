/**
 * ApplicationOption.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.core;

import java.util.Hashtable;

public class ApplicationOption {

	private Application application;
	private String messageKey;
	private String screenClass;
	private Hashtable params;

	public ApplicationOption(Application application) {
		this(application, null, null, new Hashtable());
	}
	
	public ApplicationOption(Application application, String messageKey, String screenClass, Hashtable params) {
		this.application = application;
		this.messageKey = messageKey;
		this.screenClass = screenClass;
		this.params = params;
	}

	public Application getApplication() {
		return application;
	}
	
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public String getScreenClass() {
		return screenClass;
	}

	public void setScreenClass(String screenClass) {
		this.screenClass = screenClass;
	}
	
	public Hashtable getParams() {
		return params;
	}
	
	public void setParams(Hashtable params) {
		this.params = params;
	}
}
