/*
 * Copyright 2002 - 2015 Webdetails, a Pentaho company. All rights reserved.
 *
 * This software was developed by Webdetails and is provided under the terms
 * of the Mozilla Public License, Version 2.0, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://mozilla.org/MPL/2.0/. The Initial Developer is Webdetails.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 */

package org.pentaho.det.impl.domain.mapper;


public class UnableToConvertException extends Exception {

    private Object originalValue;

    public UnableToConvertException() {
        super();
    }
    public UnableToConvertException( String message ) {
        super( message );
    }
    public UnableToConvertException( Throwable cause ) {
        super( cause );
    }
    public UnableToConvertException( String message, Throwable cause ) {
        super( message, cause );
    }

    public UnableToConvertException( Object value, Throwable cause ) {
        this( getMessage( value ), cause );
        this.originalValue = value;
    }

    public Object getOriginalValue() {
        return this.originalValue;
    }

    private static String getMessage( Object value ) {
        return "Unable to convert object from class " + value.getClass().getSimpleName();
    };
}