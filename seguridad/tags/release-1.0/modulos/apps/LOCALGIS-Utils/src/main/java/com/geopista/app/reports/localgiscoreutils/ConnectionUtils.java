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
