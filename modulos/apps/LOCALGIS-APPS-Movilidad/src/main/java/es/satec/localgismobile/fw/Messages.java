/**
 * Messages.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is licensed and may be used, modified and redistributed under the terms of the European Public License (EUPL), either version 1.1 or (at your option) any later version as soon as they are approved by the European Commission.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and more details.
 * You should have received a copy of the EUPL1.1 license along with this program; if not, you may find it at http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 */
package es.satec.localgismobile.fw;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class Messages {
	
	private static ResourceBundle messages;
	
	private static Logger logger = Global.getLoggerFor(Messages.class);
	
	static {
		String language = Config.prLocalgis.getProperty(Config.PROPERTY_LOCALE_LANGUAGE);
		String country = Config.prLocalgis.getProperty(Config.PROPERTY_LOCALE_COUNTRY);
		String messagesPath = Config.prLocalgis.getProperty(Config.PROPERTY_MESSAGES_PATH);
		Locale locale = new Locale(language, country);
		messages = ResourceBundle.getBundle(messagesPath, locale);
	}
	
	public static String getMessage(String key) {
		try {
			return messages.getString(key);
		} catch (MissingResourceException e) {
			logger.error(e);
			return "?" + key +"?";
		}
	}

}
