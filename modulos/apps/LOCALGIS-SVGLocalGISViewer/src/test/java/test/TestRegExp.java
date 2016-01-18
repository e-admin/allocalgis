/**
 * TestRegExp.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package test;

import es.satec.regexp.Matcher;
import es.satec.regexp.Pattern;

public class TestRegExp {

	public static void main(String args[]) throws Exception {
		Pattern p = Pattern.compile("a*b");
		Matcher m = p.matcher("aaaaab");
		boolean b = m.matches();
		System.out.println(b);
		
		/********** Simple Word Replacement ***********/
		//		 Create a pattern to match cat
        p = Pattern.compile("cat");
        // Create a matcher with an input string
        m = p.matcher("one cat," +
                       " two cats in the yard");
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        // Loop through and create a new String 
        // with the replacements
        while(result) {
            m.appendReplacement(sb, "dog");
            result = m.find();
        }
        // Add the last segment of input to 
        // the new String
        m.appendTail(sb);
        System.out.println(sb.toString());

        /*********** Email Validation *************************/
        String input = "@sun.com";
        //Checks for email addresses starting with
        //inappropriate symbols like dots or @ signs.
        p = Pattern.compile("^\\.|^\\@");
        m = p.matcher(input);
        if (m.find())
           System.err.println("Email addresses don't start" +
                              " with dots or @ signs.");
        //Checks for email addresses that start with
        //www. and prints a message if it does.
        p = Pattern.compile("^www\\.");
        m = p.matcher(input);
        if (m.find()) {
          System.out.println("Email addresses don't start" +
                  " with \"www.\", only web pages do.");
        }
        p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
        m = p.matcher(input);
        sb = new StringBuffer();
        result = m.find();
        boolean deletedIllegalChars = false;

        while(result) {
           deletedIllegalChars = true;
           m.appendReplacement(sb, "");
           result = m.find();
        }

        // Add the last segment of input to the new String
        m.appendTail(sb);

        input = sb.toString();

        if (deletedIllegalChars) {
           System.out.println("It contained incorrect characters" +
                             " , such as spaces or commas.");
        }


        /*********************************/
        String url = ("http://172.22.190.132:8080/direct/mms");
        String url2 = ("http://172.22.190.132:8080/direct/mms|((.)+/envms/downloader)");

        Pattern pt = Pattern.compile(url);
        /*
         UriRegexCondition uriRege = new UriRegexCondition(pt);

         System.out.println("Test.main 1 ->" + uriRege.matches("http://172.22.190.132:8080/direct/mms/dsf/aasd:asdda"));
         System.out.println("Test.main 2 ->" + uriRege.matches("http://172.22.190.132:9090/envms/downloader "));
         System.out.println("Test.main 3 ->" + uriRege.matches("http://172.17.190.24:8181/envms/downloader?id=asdasd/asdasd/ "));
         System.out.println("Test.main 4 ->" + uriRege.matches("direct/"));

        */
        Pattern pt2 = Pattern.compile(url2);

        UriRegexCondition uriRege2 = new UriRegexCondition(pt2);

        System.out.println("Test.main  5 -->" + uriRege2.matches("http://172.22.190.132:8080/direct/mms/dsf/aasd:asdda"));
        System.out.println("Test.main  6 -->" + uriRege2.matches("http://172.17.62.24:8181/envms/downloader?id=/asd/sdss"));
        System.out.println("Test.main  7 -->" + uriRege2.matches("s/satec/mms"));
        System.out.println("Test.main  8 -->" + uriRege2.matches("http://172.17.62.24:8181/enms/downloader?id=/asdasd/asd"));


        String st1 = "(\\x8c[\\x83\\x85]\\x98peakm\\.[0-9a-f]+\\x00)|\\x8c[\\x83\\x85]\\x98/envms(.)+";
        //\x8c[\x83\x85]\x98peakm\.[0-9a-f]+\x00)|envms\\.[0-9a-f]+\\x00
        Pattern pt1 = Pattern.compile(st1);

//        BodyRegexCondition bd = new BodyRegexCondition(pt1);
//
//        byte[] b1 = {(byte) 0x8c,
//                     (byte) 0x83,
//                     (byte) 0x98,
//                     (byte) 'p',
//                     (byte) 'e',
//                     (byte) 'a',
//                     (byte) 'k',
//                     (byte) 'm',
//                     (byte) '.',
//                     (byte) '1',
//                     (byte) '2',
//                     (byte) '3',
//                     (byte) 0x00};
//
//
//        byte[] b2 = {(byte) 0x8c,
//                     (byte) 0x83,
//                     (byte) 0x98,
//                     (byte) '/',
//                     (byte) 'e',
//                     (byte) 'n',
//                     (byte) 'v',
//                     (byte) 'm',
//                     (byte) 's',
//                     (byte) '/',
//                     (byte) '2',
//                     (byte) '3',
//                     (byte) '2',
//                     (byte) '3',
//                     (byte) 0x00};
//
//
//        System.out.println("Test.main byte 1 " + bd.matches(b1));
//        System.out.println("Test.main byte 2 " + bd.matches(b2));

	}
	
}
