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

package org.pentaho.det.impl.domain.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RowValueAdapter extends XmlAdapter<Object, Object> {

    private static final Pattern _dateIso8601Pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{1,3}Z");

    /**
     *
     * @param value row value
     * @return a calendar if the string represents a date, otherwise returns the value unchanged
     * @throws Exception
     */
    @Override
    public Object unmarshal( Object value ) throws Exception {

        String stringValue = value.toString();
        Matcher match = _dateIso8601Pattern.matcher( stringValue );
        if( match.find() ) {
            return this.calendarUnmarshal( stringValue );
        }

        return value;
    }

    /**
     * Function that will take the String representing a date according to ISO 8601, and return
     * a calendar of that date.
     *
     * @param value ISO8601 String representation of a date
     * @return string representation of the date value
     * @throws Exception
     */
    public Object calendarUnmarshal( String value ) throws Exception {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'hh:mm:ss.S'Z'" );
        dateFormat.setTimeZone( tz );

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone( tz );
        calendar.setTime( dateFormat.parse( value ) );

        return calendar;
    }

    /**
     *
     * @param rowValue row value
     * @return a string if rowValue is a Calendar, otherwise returns rowValue unchanged
     * @throws Exception
     */
    @Override
    public Object marshal( Object rowValue ) throws Exception {
        if( rowValue instanceof Calendar) { //ugly
            return this.marshal( (Calendar) rowValue );
        }

        return rowValue;
    }

    /**
     * Function that will format the Calendar value and return a string representation of it,
     * according to ISO 8601
     *
     * @param rowValue date to be formatted
     * @return string representation of the date value
     * @throws Exception
     */
    public Object marshal( Calendar rowValue ) throws  Exception {
        DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.S'Z'" );
        df.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        return df.format( rowValue.getTime() );
    }
}
