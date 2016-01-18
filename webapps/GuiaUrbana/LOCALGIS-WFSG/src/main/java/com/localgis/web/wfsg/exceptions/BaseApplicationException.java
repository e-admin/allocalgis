/**
 * BaseApplicationException.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.wfsg.exceptions;

/**
 * A base class for all application exceptions.
 */
public class BaseApplicationException extends ApplicationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -835628429786179728L;
	/**
	 * 
	 */
	// the error code of this exception.
    private String errorCode;

    /**
     * Constructs a new application exception with the given error code.
     * @param errorCode The error code of the exception.
     */
    public BaseApplicationException(String errorCode) {
        this(errorCode, errorCode);
    }

    /**
     * Constructs a new application exception.
     * @param errorCode The error code of the exception.
     * @param message The message of the exception.
     */
    public BaseApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructs a new application exception.
     * @param errorCode The error code of the exception.
     * @param cause The cause for the exception.
     */
    public BaseApplicationException(String errorCode, Throwable cause) {
        this(errorCode, errorCode, cause);
    }

    /**
     * Constructs a new application exception.
     * @param errorCode The error code of the exception.
     * @param message The message of the exception.
     * @param cause The cause for the exception.
     */
    public BaseApplicationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code of this exception.
     * @return The error code of this exception.
     * @see ApplicationException#getErrorCode()
     */
    public String getErrorCode() {
        return errorCode;
    }

}
