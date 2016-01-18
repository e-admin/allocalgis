package test;

import es.satec.regexp.Pattern;

	public   class UriRegexCondition

	   {

	       private Pattern pattern;

	       public boolean matches(String uri)
	       {


	           if(uri == null)
	               return false;
	           else
	               return pattern.matcher(uri).lookingAt();
	       }

	       public String toString()
	       {
	           return "UriRegexCondition[" + pattern.pattern() + "]";
	       }

	       public UriRegexCondition(Pattern pattern)
	       {
	           this.pattern = pattern;
	       }
	   }
