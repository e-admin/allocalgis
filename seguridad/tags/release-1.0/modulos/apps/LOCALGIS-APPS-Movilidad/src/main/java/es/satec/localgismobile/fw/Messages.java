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
