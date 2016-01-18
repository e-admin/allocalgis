/**
 * VersionImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.tools.modules.impl;

import java.util.StringTokenizer;

import com.localgis.tools.modules.ComparableVersion;
import com.localgis.tools.modules.Version;

/**
 * Modela la numeraci√≥n de las versiones de acuerdo al criterio de MAVEN
 * {@link http://docs.codehaus.org/display/MAVEN/Versioning}
 * 
 * @author juacas
 *
 */
public class VersionImpl implements Version {
	  private Integer majorVersion;

	    private Integer minorVersion;

	    private Integer incrementalVersion;

	    private Integer buildNumber;

	    private String qualifier;

	    private ComparableVersion comparable;

	    public VersionImpl( String version )
	    {
	        parseVersion( version );
	    }

	    @Override
	    public int hashCode()
	    {
	        return 11 + comparable.hashCode();
	    }

	    @Override
	    public boolean equals( Object other )
	    {
	        return compareTo( other ) == 0;
	    }

	    public int compareTo( Object o )
	    {
	        VersionImpl otherVersion = (VersionImpl) o;
	        return this.comparable.compareTo( otherVersion.comparable );
	    }

	    /* (non-Javadoc)
		 * @see com.localgis.tools.modules.Version#getMajorVersion()
		 */
	    public int getMajorVersion()
	    {
	        return majorVersion != null ? majorVersion : 0;
	    }

	    /* (non-Javadoc)
		 * @see com.localgis.tools.modules.Version#getMinorVersion()
		 */
	    public int getMinorVersion()
	    {
	        return minorVersion != null ? minorVersion : 0;
	    }

	    /* (non-Javadoc)
		 * @see com.localgis.tools.modules.Version#getIncrementalVersion()
		 */
	    public int getIncrementalVersion()
	    {
	        return incrementalVersion != null ? incrementalVersion : 0;
	    }

	    /* (non-Javadoc)
		 * @see com.localgis.tools.modules.Version#getBuildNumber()
		 */
	    public int getBuildNumber()
	    {
	        return buildNumber != null ? buildNumber : 0;
	    }

	    /* (non-Javadoc)
		 * @see com.localgis.tools.modules.Version#getQualifier()
		 */
	    public String getQualifier()
	    {
	        return qualifier;
	    }

	    /* (non-Javadoc)
		 * @see com.localgis.tools.modules.Version#parseVersion(java.lang.String)
		 */
	    public final void parseVersion( String version )
	    {
	        comparable = new ComparableVersion( version );

	        int index = version.indexOf( "-" );

	        String part1;
	        String part2 = null;

	        if ( index < 0 )
	        {
	            part1 = version;
	        }
	        else
	        {
	            part1 = version.substring( 0, index );
	            part2 = version.substring( index + 1 );
	        }

	        if ( part2 != null )
	        {
	            try
	            {
	                if ( ( part2.length() == 1 ) || !part2.startsWith( "0" ) )
	                {
	                    buildNumber = Integer.valueOf( part2 );
	                }
	                else
	                {
	                    qualifier = part2;
	                }
	            }
	            catch ( NumberFormatException e )
	            {
	                qualifier = part2;
	            }
	        }

	        if ( ( part1.indexOf( "." ) < 0 ) && !part1.startsWith( "0" ) )
	        {
	            try
	            {
	                majorVersion = Integer.valueOf( part1 );
	            }
	            catch ( NumberFormatException e )
	            {
	                // qualifier is the whole version, including "-"
	                qualifier = version;
	                buildNumber = null;
	            }
	        }
	        else
	        {
	            boolean fallback = false;

	            StringTokenizer tok = new StringTokenizer( part1, "." );
	            try
	            {
	                majorVersion = getNextIntegerToken( tok );
	                if ( tok.hasMoreTokens() )
	                {
	                    minorVersion = getNextIntegerToken( tok );
	                }
	                if ( tok.hasMoreTokens() )
	                {
	                    incrementalVersion = getNextIntegerToken( tok );
	                }
	                if ( tok.hasMoreTokens() )
	                {
	                    fallback = true;
	                }

	                // string tokenzier won't detect these and ignores them
	                if ( part1.indexOf( ".." ) >= 0 || part1.startsWith( "." ) || part1.endsWith( "." ) )
	                {
	                    fallback = true;
	                }
	            }
	            catch ( NumberFormatException e )
	            {
	                fallback = true;
	            }

	            if ( fallback )
	            {
	                // qualifier is the whole version, including "-"
	                qualifier = version;
	                majorVersion = null;
	                minorVersion = null;
	                incrementalVersion = null;
	                buildNumber = null;
	            }
	        }
	    }

	    private static Integer getNextIntegerToken( StringTokenizer tok )
	    {
	        String s = tok.nextToken();
	        if ( ( s.length() > 1 ) && s.startsWith( "0" ) )
	        {
	            throw new NumberFormatException( "Number part has a leading 0: '" + s + "'" );
	        }
	        return Integer.valueOf( s );
	    }

	    @Override
	    public String toString()
	    {
	        StringBuffer buf = new StringBuffer();
	        if ( majorVersion != null )
	        {
	            buf.append( majorVersion );
	        }
	        if ( minorVersion != null )
	        {
	            buf.append( "." );
	            buf.append( minorVersion );
	        }
	        if ( incrementalVersion != null )
	        {
	            buf.append( "." );
	            buf.append( incrementalVersion );
	        }
	        if ( buildNumber != null )
	        {
	            buf.append( "-" );
	            buf.append( buildNumber );
	        }
	        else if ( qualifier != null )
	        {
	            if ( buf.length() > 0 )
	            {
	                buf.append( "-" );
	            }
	            buf.append( qualifier );
	        }
	        return buf.toString();
	    }

}
