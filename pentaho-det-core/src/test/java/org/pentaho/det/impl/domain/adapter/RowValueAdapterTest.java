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


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class RowValueAdapterTest {

    @Test
    public void testDateMarshal() throws Exception {
        RowValueAdapter adapter = this.createRowValueAdapter();

        Object value = this.createCalendar( "yyyy/MM/dd HH:mm:ss.S", "2000/04/25 13:37:00.666" );
        Object expectedResult = "2000-04-25T13:37:00.666Z";

        Object result = adapter.marshal( value );
        assertThat( result, is( equalTo( expectedResult ) ) );
    }

    @Test
    public void testIntegerMarshal() throws Exception {
        RowValueAdapter adapter = this.createRowValueAdapter();

        Object value = 9999;

        Object result = adapter.marshal( value );
        assertThat( result, is( equalTo( value ) ) );

    }

    @Test
    public void testBooleanMarshal() throws Exception {
        RowValueAdapter adapter = this.createRowValueAdapter();

        Object value = true;

        Object result = adapter.marshal( value );
        assertThat( result, is( equalTo( value ) ) );

    }

    @Test
    public void testStringMarshal() throws Exception {
        RowValueAdapter adapter = this.createRowValueAdapter();

        Object value = "FooBar";

        Object result = adapter.marshal( value );
        assertThat( result, is( equalTo( value ) ) );
    }

    @Test
    public void testRowValueAdapterUnmarshall() throws Exception {
        RowValueAdapter adapter = this.createRowValueAdapter();

        Object value = "FooBar";

        Object result = adapter.unmarshal( value );
        assertThat( result, is( equalTo( value ) ) );
    }

    private RowValueAdapter createRowValueAdapter() {
        return new RowValueAdapter();
    }

    private Calendar createCalendar( String format, String date ) throws Exception {
        TimeZone tz = TimeZone.getTimeZone( "UTC" );
        SimpleDateFormat dateFormat = new SimpleDateFormat( format );
        dateFormat.setTimeZone( tz );

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone( tz );
        calendar.setTime( dateFormat.parse( date ) );

        return calendar;
    }
}
