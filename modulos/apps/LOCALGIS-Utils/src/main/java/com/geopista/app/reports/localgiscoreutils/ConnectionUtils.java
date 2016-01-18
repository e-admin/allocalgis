/**
 * ConnectionUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.localgiscoreutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionUtils {
	
	private static final Pattern oracleConnectionPattern = Pattern.compile("jdbc:oracle:(\\w+):@(.+):(\\d+):(\\w+)");
	private static final Pattern postgresConnectionPattern = Pattern.compile("jdbc:postgresql://(.+):(\\d+)/(\\w+)");
	private static final Pattern postgresConnectionPatternDefaultPort = Pattern.compile("jdbc:postgresql://(.+)/(\\w+)");
	
	public static String getDatatabaseType(String jdbcUrl){
		Matcher oraclePatternMatcher = oracleConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternMatcher = postgresConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternDefaultPortMatcher = postgresConnectionPatternDefaultPort.matcher(jdbcUrl);
		
		if (oraclePatternMatcher.matches()){
			return "oracle";
		}
		else if (postgresPatternMatcher.matches() || postgresPatternDefaultPortMatcher.matches()){
			return "postgis";
		}
		else {
			return null;
		}
	}
	
	public static String getPort(String jdbcUrl){
		Matcher oraclePatternMatcher = oracleConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternMatcher = postgresConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternDefaultPortMatcher = postgresConnectionPatternDefaultPort.matcher(jdbcUrl);
		
		if (oraclePatternMatcher.matches()){
			return oraclePatternMatcher.group(3);
		}
		else if (postgresPatternMatcher.matches()){
			return postgresPatternMatcher.group(2);
		}
		else if (postgresPatternDefaultPortMatcher.matches()){
			return "5432";
		}
		else {
			return null;
		}
	}
	
	public static String getHostname(String jdbcUrl){
		Matcher oraclePatternMatcher = oracleConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternMatcher = postgresConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternDefaultPortMatcher = postgresConnectionPatternDefaultPort.matcher(jdbcUrl);
		
		if (oraclePatternMatcher.matches()){
			return oraclePatternMatcher.group(2);
		}
		else if (postgresPatternMatcher.matches()){
			return postgresPatternMatcher.group(1);
		}
		else if (postgresPatternDefaultPortMatcher.matches()){
			return postgresPatternDefaultPortMatcher.group(1);
		}
		else {
			return null;
		}
	}
	
	public static String getDatabaseName(String jdbcUrl){
		Matcher oraclePatternMatcher = oracleConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternMatcher = postgresConnectionPattern.matcher(jdbcUrl);
		Matcher postgresPatternDefaultPortMatcher = postgresConnectionPatternDefaultPort.matcher(jdbcUrl);
		
		if (oraclePatternMatcher.matches()){
			return oraclePatternMatcher.group(4);
		}
		else if (postgresPatternMatcher.matches()){
			return postgresPatternMatcher.group(3);
		}
		else if (postgresPatternDefaultPortMatcher.matches()){
			return postgresPatternDefaultPortMatcher.group(2);
		}
		else {
			return null;
		}
	}
}
